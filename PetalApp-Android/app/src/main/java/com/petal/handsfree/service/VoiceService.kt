package com.petal.handsfree.service

import android.app.*
import com.google.firebase.crashlytics.FirebaseCrashlytics
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.os.PowerManager
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.petal.handsfree.BuildConfig
import com.petal.handsfree.MainActivity
import com.petal.handsfree.R
import com.petal.handsfree.client.GeminiClient
import com.petal.handsfree.processor.VoiceProcessor
import com.petal.handsfree.utils.TtsHelper
import com.petal.handsfree.wake.VoskWakeWordDetector
import com.petal.handsfree.wake.WakeWordDetector
import kotlinx.coroutines.*
import java.util.*

class VoiceService : LifecycleService(), RecognitionListener, TextToSpeech.OnInitListener {

    companion object {
        const val ACTION_START_SERVICE = "com.petal.handsfree.START_SERVICE"
        const val ACTION_STOP_SERVICE = "com.petal.handsfree.STOP_SERVICE"
        const val ACTION_PROCESS_TEXT_COMMAND = "com.petal.handsfree.PROCESS_TEXT_COMMAND"
        const val EXTRA_TEXT_COMMAND = "extra_text_command"

        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "PetalHandsfreeChannel"
        private const val TAG = "VoiceService"

        val currentState = MutableLiveData<VoiceState?>(null)

        @Volatile
        var isRunning: Boolean = false
            private set
    }

    enum class VoiceState { LISTENING, WAKE_WORD_DETECTED, PROCESSING }

    private var speechRecognizer: SpeechRecognizer? = null
    private var textToSpeech: TextToSpeech? = null
    private var voiceProcessor: VoiceProcessor? = null
    private var ttsHelper: TtsHelper? = null
    private var wakeLock: PowerManager.WakeLock? = null
    private var notificationManager: NotificationManager? = null
    private var wakeDetector: VoskWakeWordDetector? = null

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private var isSpeechRecognizerListening = false
    private var isTtsReady = false
    private var isCapturingCommand = false

    // ─── WakeWordDetector.Listener ────────────────────────────────────────────

    private val wakeWordListener = object : WakeWordDetector.Listener {

        override fun onWakeWordDetected(matchedPhrase: String, trailingCommand: String) {
            if (!isRunning) return
            Log.i(TAG, "Wake word detected: '$matchedPhrase' trailing='$trailingCommand'")

            if (trailingCommand.isNotBlank()) {
                // Full command came in one breath — skip the SpeechRecognizer round-trip
                currentState.postValue(VoiceState.PROCESSING)
                serviceScope.launch { updateNotification("Procesando...") }
                processCommand(trailingCommand)
            } else {
                // Prompt the user to say their command
                currentState.postValue(VoiceState.WAKE_WORD_DETECTED)
                serviceScope.launch {
                    updateNotification("¿Qué necesitas?")
                    ttsHelper?.speak("Dime") {}
                    // Pause VOSK so SpeechRecognizer can own the mic
                    wakeDetector?.pause()
                    isCapturingCommand = true
                    startCommandCapture()
                }
            }
        }

        override fun onError(error: Throwable) {
            Log.e(TAG, "Wake word detector error", error)
            // Try to restart the detector after a short delay
            if (isRunning) {
                serviceScope.launch {
                    delay(2000)
                    if (isRunning) {
                        try { wakeDetector?.resume() } catch (e: Exception) {
                            Log.e(TAG, "Failed to resume wake detector", e)
                        }
                    }
                }
            }
        }
    }

    // ─── Lifecycle ────────────────────────────────────────────────────────────

    override fun onCreate() {
        super.onCreate()
        initializeService()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        when (intent?.action) {
            ACTION_START_SERVICE -> startHandsfreeMode()
            ACTION_STOP_SERVICE -> stopSelf()
            ACTION_PROCESS_TEXT_COMMAND -> {
                val command = intent.getStringExtra(EXTRA_TEXT_COMMAND)
                if (!command.isNullOrBlank()) processCommand(command)
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    override fun onDestroy() {
        cleanupService()
        super.onDestroy()
    }

    private fun initializeService() {
        createNotificationChannel()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK, "PetalApp::VoiceServiceWakeLock"
        ).apply { setReferenceCounted(false) }

        val geminiClient = GeminiClient(
            apiKey = BuildConfig.GEMINI_API_KEY,
            baseUrl = BuildConfig.GEMINI_BASE_URL,
            onLog = { Log.w("GeminiClient", it) },
            onNonFatal = { if (!BuildConfig.DEBUG) FirebaseCrashlytics.getInstance().recordException(it) }
        )
        voiceProcessor = VoiceProcessor(this, geminiClient)
        wakeDetector = VoskWakeWordDetector(this)
        ttsHelper = TtsHelper(this) { ready ->
            isTtsReady = ready
            if (ready) ttsHelper?.speak("Petal activo. Di Oye Petal para hablar conmigo.") {}
        }
        textToSpeech = TextToSpeech(this, this)
    }

    private fun startHandsfreeMode() {
        if (isRunning) return
        startForeground(NOTIFICATION_ID, createNotification("Cargando..."))
        wakeLock?.acquire(10 * 60 * 1000L)
        isRunning = true

        serviceScope.launch {
            // Load the Vosk model (heavy IO — can take a few seconds on first run)
            updateNotification("Cargando modelo de voz...")
            withContext(Dispatchers.IO) {
                try {
                    wakeDetector?.initialize()
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to initialize wake word detector", e)
                    return@withContext
                }
            }
            // Wait for TTS so the greeting plays properly
            waitForTtsReady()

            // Start continuous VOSK wake word detection
            try {
                wakeDetector?.start(wakeWordListener)
                currentState.postValue(VoiceState.LISTENING)
                updateNotification("Di «Oye Petal»")
                Log.i(TAG, "Continuous wake word detection started")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to start wake word detection", e)
                updateNotification("Error al iniciar — Reinicia")
            }
        }
    }

    private suspend fun waitForTtsReady() {
        repeat(50) { if (!isTtsReady) delay(100) }
    }

    // ─── SpeechRecognizer (command capture only) ──────────────────────────────

    private fun startCommandCapture() {
        if (isSpeechRecognizerListening) return

        speechRecognizer?.destroy()
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this).apply {
            setRecognitionListener(this@VoiceService)
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
            putExtra("android.speech.extra.SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS", 2000L)
            putExtra("android.speech.extra.SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS", 1500L)
        }

        try {
            speechRecognizer?.startListening(intent)
            isSpeechRecognizerListening = true
            Log.d(TAG, "SpeechRecognizer started (command capture)")
        } catch (e: Exception) {
            Log.e(TAG, "Error starting SpeechRecognizer", e)
            returnToWakeWordListening()
        }
    }

    private fun returnToWakeWordListening() {
        isCapturingCommand = false
        isSpeechRecognizerListening = false
        speechRecognizer?.destroy()
        speechRecognizer = null
        currentState.postValue(VoiceState.LISTENING)
        updateNotification("Di «Oye Petal»")
        serviceScope.launch {
            try { wakeDetector?.resume() } catch (e: Exception) {
                Log.e(TAG, "Failed to resume wake detector", e)
            }
        }
    }

    // ─── RecognitionListener ──────────────────────────────────────────────────

    override fun onReadyForSpeech(params: Bundle?) {}

    override fun onBeginningOfSpeech() {}

    override fun onRmsChanged(rmsdB: Float) {}

    override fun onBufferReceived(buffer: ByteArray?) {}

    override fun onEndOfSpeech() {
        isSpeechRecognizerListening = false
    }

    override fun onError(error: Int) {
        isSpeechRecognizerListening = false
        Log.d(TAG, "SpeechRecognizer onError: $error (capturing=$isCapturingCommand)")

        if (isCapturingCommand) {
            when (error) {
                SpeechRecognizer.ERROR_NO_MATCH,
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> {
                    // User didn't say anything after wake word — return to VOSK
                    Log.d(TAG, "No command heard — returning to wake word listening")
                    returnToWakeWordListening()
                }
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY,
                SpeechRecognizer.ERROR_CLIENT -> {
                    serviceScope.launch {
                        delay(300)
                        if (isCapturingCommand && isRunning) startCommandCapture()
                    }
                }
                SpeechRecognizer.ERROR_NETWORK,
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT,
                SpeechRecognizer.ERROR_SERVER -> {
                    Log.w(TAG, "Network error during command capture — returning to VOSK")
                    returnToWakeWordListening()
                }
                else -> returnToWakeWordListening()
            }
        }
    }

    override fun onResults(results: Bundle?) {
        isSpeechRecognizerListening = false
        val text = results
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            ?.firstOrNull()
            ?.lowercase(Locale("es"))
            ?.trim()

        if (text.isNullOrBlank()) {
            returnToWakeWordListening()
            return
        }

        Log.i(TAG, "Command recognized: '$text'")
        isCapturingCommand = false
        currentState.postValue(VoiceState.PROCESSING)
        updateNotification("Procesando...")
        processCommand(text)
    }

    override fun onPartialResults(partialResults: Bundle?) {}

    override fun onEvent(eventType: Int, params: Bundle?) {}

    // ─── TextToSpeech ─────────────────────────────────────────────────────────

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech?.language = Locale("es", "ES")
            isTtsReady = true
        } else {
            Log.e(TAG, "TTS init failed")
        }
    }

    // ─── Command processing ───────────────────────────────────────────────────

    private fun processCommand(command: String) {
        serviceScope.launch {
            try {
                voiceProcessor?.processVoiceCommand(command) { response ->
                    ttsHelper?.speak(response) {
                        serviceScope.launch {
                            currentState.postValue(VoiceState.LISTENING)
                            updateNotification("Di «Oye Petal»")
                            // If SpeechRecognizer was open, resume VOSK
                            if (!isCapturingCommand) {
                                try { wakeDetector?.resume() } catch (e: Exception) {
                                    Log.e(TAG, "Failed to resume wake detector after command", e)
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error processing command", e)
                if (!BuildConfig.DEBUG) FirebaseCrashlytics.getInstance().recordException(e)
                ttsHelper?.speak("Lo siento, hubo un error") {
                    serviceScope.launch {
                        currentState.postValue(VoiceState.LISTENING)
                        updateNotification("Di «Oye Petal»")
                        try { wakeDetector?.resume() } catch (ex: Exception) {
                            Log.e(TAG, "Failed to resume wake detector after error", ex)
                        }
                    }
                }
            }
        }
    }

    // ─── Notification helpers ─────────────────────────────────────────────────

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID, "Petal Hands-free Service", NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Servicio activo para comandos de voz manos libres"
            setShowBadge(false)
            setSound(null, null)
        }
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
            .createNotificationChannel(channel)
    }

    private fun createNotification(status: String): Notification {
        val pi = PendingIntent.getActivity(
            this, 0, Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("🎤 Petal Manos Libres")
            .setContentText(status)
            .setSmallIcon(R.drawable.ic_mic)
            .setContentIntent(pi)
            .setOngoing(true)
            .setAutoCancel(false)
            .build()
    }

    private fun updateNotification(status: String) {
        notificationManager?.notify(NOTIFICATION_ID, createNotification(status))
    }

    // ─── Cleanup ──────────────────────────────────────────────────────────────

    private fun cleanupService() {
        isRunning = false
        isCapturingCommand = false
        currentState.postValue(null)

        isSpeechRecognizerListening = false
        speechRecognizer?.destroy()
        speechRecognizer = null

        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null

        wakeLock?.let { if (it.isHeld) it.release() }
        wakeLock = null

        serviceScope.launch {
            try { wakeDetector?.release() } catch (e: Exception) {
                Log.e(TAG, "Error releasing wake detector", e)
            }
        }

        serviceJob.cancel()
        voiceProcessor = null
        ttsHelper = null
        wakeDetector = null
    }
}
