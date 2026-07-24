package com.petal.handsfree.wake

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runInterruptible
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import org.json.JSONObject
import org.vosk.LibVosk
import org.vosk.LogLevel
import org.vosk.Model
import org.vosk.Recognizer
import org.vosk.android.RecognitionListener
import org.vosk.android.SpeechService
import java.io.File
import java.util.regex.Pattern

/**
 * Wake-word detector backed by Vosk (offline, open-source).
 *
 * We bypass StorageService.unpack() because its sync() method writes files to
 * externalFilesDir/<target>/ but creates the Model with path
 * externalFilesDir/<target>/<source>, which is a non-existent subdirectory.
 * Instead we extract directly to filesDir/vosk-model/ so the path is exact.
 */
class VoskWakeWordDetector(
    private val context: Context
) : WakeWordDetector {

    companion object {
        private const val TAG = "VoskWakeWord"
        private const val ASSETS_MODEL_DIR = "model-es"
        private const val MODEL_DIR_NAME   = "vosk-model"
        private const val SAMPLE_RATE      = 16_000.0f
        // Hard cap on how long we wait for Vosk's recognizerThread to exit.
        // If AudioRecord.read() is stuck, runInterruptible delivers Thread.interrupt()
        // which unblocks it; withTimeoutOrNull abandons the wait after this limit.
        private const val SHUTDOWN_TIMEOUT_MS = 1500L

        // No grammar restriction — free transcription gives better recall.
        // "petal" is not in the Spanish Vosk model vocab; it gets transcribed
        // as "pétalo", "petalo", "petar", "peta", or "pedal" depending on the
        // speaker. We match all of them in WAKE_PATTERN.
        private val WAKE_PATTERN: Pattern = Pattern.compile(
            "(?:oye|ey|hey)\\s+(?:petal|p[eé]tal[oa]?|petar|peta|pedal)" +
                "|(?:petal|p[eé]tal[oa]?|petar|peta|pedal)",
            Pattern.CASE_INSENSITIVE
        )
    }

    @Volatile private var model: Model? = null
    @Volatile private var speechService: SpeechService? = null
    @Volatile private var listener: WakeWordDetector.Listener? = null

    @Volatile override var isListening: Boolean = false
        private set

    @Volatile private var isPaused: Boolean = false

    init {
        try { LibVosk.setLogLevel(LogLevel.WARNINGS) } catch (_: Throwable) {}
    }

    override suspend fun initialize() {
        if (model != null) return

        withContext(Dispatchers.IO) {
            val modelDir = File(context.filesDir, MODEL_DIR_NAME)

            val assetUuid = context.assets.open("$ASSETS_MODEL_DIR/uuid")
                .bufferedReader().use { it.readLine()?.trim() ?: "" }
            val uuidFile  = File(modelDir, "uuid")
            val diskUuid  = if (uuidFile.exists()) uuidFile.readText().trim() else null

            if (diskUuid != assetUuid) {
                Log.i(TAG, "Extracting Vosk model → ${modelDir.absolutePath}")
                modelDir.deleteRecursively()
                modelDir.mkdirs()
                copyAssetTree(ASSETS_MODEL_DIR, modelDir)
                uuidFile.writeText(assetUuid)
                Log.i(TAG, "Model extracted OK")
            } else {
                Log.i(TAG, "Using cached Vosk model at ${modelDir.absolutePath}")
            }

            Log.i(TAG, "Loading Model from ${modelDir.absolutePath}")
            model = Model(modelDir.absolutePath)
            Log.i(TAG, "Vosk model loaded OK")
        }
    }

    private fun copyAssetTree(assetPath: String, targetDir: File) {
        val children = context.assets.list(assetPath) ?: emptyArray()
        if (children.isEmpty()) {
            targetDir.parentFile?.mkdirs()
            context.assets.open(assetPath).use { src ->
                targetDir.outputStream().use { dst -> src.copyTo(dst) }
            }
        } else {
            targetDir.mkdirs()
            for (child in children) {
                copyAssetTree("$assetPath/$child", File(targetDir, child))
            }
        }
    }

    override suspend fun start(listener: WakeWordDetector.Listener) {
        this.listener = listener
        isPaused = false
        if (isListening) return
        withContext(Dispatchers.IO) { startSpeechServiceInternal() }
    }

    /**
     * Pause releases the microphone completely so SpeechRecognizer can use it.
     *
     * State flags are set BEFORE calling shutdown so the detector is in a
     * consistent state even if the native shutdown throws or times out.
     */
    override suspend fun pause() {
        if (!isListening || isPaused) return

        // State first, side-effects after — keeps the detector coherent if JNI crashes.
        val service = speechService
        speechService = null
        isListening   = false
        isPaused      = true

        shutdownServiceWithTimeout(service)
        Log.d(TAG, "Vosk paused (mic released)")
    }

    override suspend fun resume() {
        if (!isPaused) {
            if (!isListening && listener != null) withContext(Dispatchers.IO) { startSpeechServiceInternal() }
            return
        }
        isPaused = false
        withContext(Dispatchers.IO) { startSpeechServiceInternal() }
        Log.d(TAG, "Vosk resumed (mic reacquired)")
    }

    override suspend fun stop() {
        val service = speechService
        speechService = null
        isListening   = false
        isPaused      = false
        shutdownServiceWithTimeout(service)
    }

    override suspend fun release() {
        stop()
        listener = null
        val m = model
        model = null
        withContext(Dispatchers.IO) {
            try { m?.close() } catch (t: Throwable) { Log.w(TAG, "Error closing model", t) }
        }
    }

    /**
     * Shuts down a Vosk SpeechService safely:
     * - Runs on IO thread (service.shutdown() calls recognizerThread.join()).
     * - Hard timeout of [SHUTDOWN_TIMEOUT_MS] so we never block forever.
     * - runInterruptible translates coroutine cancellation to Thread.interrupt(),
     *   which unblocks AudioRecord.read() inside the recognizer thread.
     * - Catches Throwable so JNI errors (UnsatisfiedLinkError, OOM, …) don't escape.
     */
    private suspend fun shutdownServiceWithTimeout(service: SpeechService?) {
        if (service == null) return
        withContext(Dispatchers.IO) {
            try {
                withTimeoutOrNull(SHUTDOWN_TIMEOUT_MS) {
                    runInterruptible { service.shutdown() }
                } ?: Log.w(TAG, "Vosk shutdown timed out after ${SHUTDOWN_TIMEOUT_MS}ms — abandoned")
            } catch (t: Throwable) {
                Log.w(TAG, "Vosk shutdown threw: ${t.javaClass.simpleName}: ${t.message}")
            }
        }
    }

    // -------------------------------------------------------------------------

    private fun startSpeechServiceInternal() {
        val loadedModel = model ?: run {
            listener?.onError(IllegalStateException("Vosk model not initialised"))
            return
        }
        if (speechService != null) return

        try {
            val recognizer = Recognizer(loadedModel, SAMPLE_RATE)
            val service    = SpeechService(recognizer, SAMPLE_RATE)
            service.startListening(internalListener)
            speechService   = service
            isListening     = true
            lastFiredPhrase = ""
            lastFiredAtMs   = 0L
            Log.i(TAG, "Vosk wake-word listener started")
        } catch (t: Throwable) {
            Log.e(TAG, "Failed to start Vosk SpeechService: ${t.javaClass.simpleName}", t)
            isListening = false
            listener?.onError(t)
        }
    }

    // -------------------------------------------------------------------------

    private val internalListener = object : RecognitionListener {

        override fun onPartialResult(hypothesis: String?) {
            val text = extractText(hypothesis, "partial") ?: return
            if (text.isBlank()) return
            Log.i(TAG, "[partial] $text")
            tryFireWakeWord(text, fromFinal = false)
        }

        override fun onResult(hypothesis: String?) {
            val text = extractText(hypothesis, "text") ?: return
            if (text.isBlank()) return
            Log.i(TAG, "[result]  $text")
            tryFireWakeWord(text, fromFinal = true)
        }

        override fun onFinalResult(hypothesis: String?) {
            val text = extractText(hypothesis, "text") ?: return
            if (text.isBlank()) return
            Log.i(TAG, "[final]   $text")
            tryFireWakeWord(text, fromFinal = true)
        }

        override fun onError(exception: Exception?) {
            Log.e(TAG, "Vosk recognition error", exception)
            listener?.onError(exception ?: RuntimeException("Unknown Vosk error"))
        }

        override fun onTimeout() {
            Log.d(TAG, "Vosk timeout (ignored)")
        }
    }

    @Volatile private var lastFiredPhrase: String = ""
    @Volatile private var lastFiredAtMs:   Long   = 0L

    private fun tryFireWakeWord(text: String, fromFinal: Boolean) {
        val matcher = WAKE_PATTERN.matcher(text)
        if (!matcher.find()) return

        val matchedPhrase = matcher.group().trim()
        val trailing      = text.substring(matcher.end()).trim()

        val now = System.currentTimeMillis()
        if (matchedPhrase.equals(lastFiredPhrase, ignoreCase = true) &&
            now - lastFiredAtMs < 1500) return

        if (!fromFinal && trailing.isEmpty() && !matchedPhrase.contains(' ')) return

        lastFiredPhrase = matchedPhrase
        lastFiredAtMs   = now
        Log.i(TAG, "Wake word: '$matchedPhrase' trailing='$trailing' final=$fromFinal")
        listener?.onWakeWordDetected(matchedPhrase, trailing)
    }

    private fun extractText(jsonHypothesis: String?, key: String): String? {
        if (jsonHypothesis.isNullOrBlank()) return null
        return try {
            JSONObject(jsonHypothesis).optString(key, "")
        } catch (e: Exception) {
            Log.w(TAG, "Cannot parse Vosk hypothesis: $jsonHypothesis", e)
            null
        }
    }
}
