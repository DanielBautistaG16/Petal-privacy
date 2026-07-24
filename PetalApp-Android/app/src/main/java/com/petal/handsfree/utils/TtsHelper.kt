package com.petal.handsfree.utils

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * TtsHelper - Text-to-Speech utility wrapper
 * 
 * Provides:
 * - Easy TTS initialization and management
 * - Speech completion callbacks
 * - Voice configuration options
 * - Error handling and recovery
 * 
 * @author Petal Team
 * @version 1.0.0
 */
class TtsHelper(
    private val context: Context,
    private val onInitialized: (Boolean) -> Unit
) : TextToSpeech.OnInitListener {

    companion object {
        private const val TAG = "TtsHelper"
        private const val UTTERANCE_ID_PREFIX = "petal_tts_"
    }

    private var textToSpeech: TextToSpeech? = null
    private var isInitialized = false
    private var utteranceIdCounter = 0
    // ConcurrentHashMap: onDone/onError fire on TTS thread, speak() on caller thread.
    private val pendingUtterances = ConcurrentHashMap<String, () -> Unit>()

    init {
        initializeTts()
    }

    /**
     * Initialize Text-to-Speech engine
     */
    private fun initializeTts() {
        try {
            textToSpeech = TextToSpeech(context, this)
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing TTS", e)
            onInitialized(false)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech?.let { tts ->
                // Set language to Spanish (Spain) by default
                val languageResult = tts.setLanguage(Locale("es", "ES"))
                
                when (languageResult) {
                    TextToSpeech.LANG_MISSING_DATA -> {
                        Log.w(TAG, "Spanish language data missing, using default")
                        tts.setLanguage(Locale.getDefault())
                    }
                    TextToSpeech.LANG_NOT_SUPPORTED -> {
                        Log.w(TAG, "Spanish language not supported, using default")
                        tts.setLanguage(Locale.getDefault())
                    }
                }
                
                // Configure TTS settings
                configureTts(tts)
                
                isInitialized = true
                Log.d(TAG, "TTS initialized successfully")
                onInitialized(true)
            }
        } else {
            Log.e(TAG, "TTS initialization failed with status: $status")
            isInitialized = false
            onInitialized(false)
        }
    }

    /**
     * Configure TTS settings for optimal cycling use
     */
    private fun configureTts(tts: TextToSpeech) {
        // Set speech rate slightly slower for better comprehension while cycling
        tts.setSpeechRate(0.9f)
        
        // Set pitch slightly higher for better clarity with wind/traffic noise
        tts.setPitch(1.1f)
        
        // Set up utterance progress listener
        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                Log.d(TAG, "TTS started: $utteranceId")
            }

            override fun onDone(utteranceId: String?) {
                Log.d(TAG, "TTS completed: $utteranceId")
                // Invoke directly — the caller (VoiceService.speakThen) wraps this
                // in serviceScope.launch to guarantee execution on the main thread.
                utteranceId?.let { id -> pendingUtterances.remove(id)?.invoke() }
            }

            override fun onError(utteranceId: String?) {
                Log.e(TAG, "TTS error: $utteranceId")
                // Always invoke so the state machine never gets stuck.
                utteranceId?.let { id -> pendingUtterances.remove(id)?.invoke() }
            }
        })
    }

    /**
     * Speak text with optional completion callback
     * 
     * @param text The text to speak
     * @param onComplete Callback when speech is completed (optional)
     */
    fun speak(text: String, onComplete: (() -> Unit)? = null) {
        if (!isInitialized || textToSpeech == null) {
            Log.w(TAG, "TTS not initialized, cannot speak: $text")
            onComplete?.invoke()   // caller (speakThen) wraps this in serviceScope.launch
            return
        }

        if (text.isBlank()) {
            onComplete?.invoke()
            return
        }

        val utteranceId = "${UTTERANCE_ID_PREFIX}${++utteranceIdCounter}"

        if (onComplete != null) {
            pendingUtterances[utteranceId] = onComplete
        }

        Log.d(TAG, "Speaking: '$text' with ID: $utteranceId")

        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
        }

        val result = textToSpeech?.speak(text, TextToSpeech.QUEUE_ADD, params, utteranceId)
        
        if (result != TextToSpeech.SUCCESS) {
            Log.e(TAG, "Failed to speak text: $text")
            pendingUtterances.remove(utteranceId)?.invoke() ?: onComplete?.invoke()
        }
    }

    /**
     * Speak text and suspend until completion (for coroutines)
     */
    suspend fun speakAndWait(text: String): Boolean = suspendCoroutine { continuation ->
        speak(text) {
            continuation.resume(true)
        }
    }

    /**
     * Stop current speech
     */
    fun stop() {
        textToSpeech?.stop()
        pendingUtterances.clear()
        Log.d(TAG, "TTS stopped")
    }

    /**
     * Check if TTS is currently speaking
     */
    fun isSpeaking(): Boolean {
        return textToSpeech?.isSpeaking ?: false
    }

    /**
     * Set speech rate
     * @param rate Speech rate (0.5f = slow, 1.0f = normal, 2.0f = fast)
     */
    fun setSpeechRate(rate: Float) {
        textToSpeech?.setSpeechRate(rate.coerceIn(0.1f, 3.0f))
        Log.d(TAG, "Speech rate set to: $rate")
    }

    /**
     * Set speech pitch
     * @param pitch Speech pitch (0.5f = low, 1.0f = normal, 2.0f = high)
     */
    fun setPitch(pitch: Float) {
        textToSpeech?.setPitch(pitch.coerceIn(0.5f, 2.0f))
        Log.d(TAG, "Speech pitch set to: $pitch")
    }

    /**
     * Set language
     * @param locale The locale to use for speech
     */
    fun setLanguage(locale: Locale): Boolean {
        return textToSpeech?.let { tts ->
            val result = tts.setLanguage(locale)
            val success = result != TextToSpeech.LANG_MISSING_DATA && 
                         result != TextToSpeech.LANG_NOT_SUPPORTED
            
            if (success) {
                Log.d(TAG, "Language set to: ${locale.displayName}")
            } else {
                Log.w(TAG, "Failed to set language: ${locale.displayName}")
            }
            
            success
        } ?: false
    }

    /**
     * Get available languages
     */
    fun getAvailableLanguages(): Set<Locale> {
        return textToSpeech?.availableLanguages ?: emptySet()
    }

    /**
     * Check if a language is available
     */
    fun isLanguageAvailable(locale: Locale): Boolean {
        return textToSpeech?.isLanguageAvailable(locale) == TextToSpeech.LANG_AVAILABLE
    }

    /**
     * Quick speak for urgent messages (interrupts current speech)
     */
    fun speakUrgent(text: String, onComplete: (() -> Unit)? = null) {
        if (!isInitialized || textToSpeech == null) {
            Log.w(TAG, "TTS not initialized, cannot speak urgent: $text")
            onComplete?.invoke()
            return
        }

        // Stop current speech
        stop()
        
        val utteranceId = "${UTTERANCE_ID_PREFIX}urgent_${++utteranceIdCounter}"
        
        if (onComplete != null) {
            pendingUtterances[utteranceId] = onComplete
        }

        Log.d(TAG, "Speaking urgently: '$text'")

        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
        }

        val result = textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
        
        if (result != TextToSpeech.SUCCESS) {
            Log.e(TAG, "Failed to speak urgent text: $text")
            onComplete?.invoke()
            pendingUtterances.remove(utteranceId)
        }
    }

    /**
     * Cleanup resources
     */
    fun shutdown() {
        stop()
        textToSpeech?.shutdown()
        textToSpeech = null
        isInitialized = false
        Log.d(TAG, "TTS shutdown completed")
    }

    /**
     * Predefined common responses for quick access
     */
    object CommonPhrases {
        const val LISTENING = "Te escucho"
        const val PROCESSING = "Procesando"
        const val ERROR = "Lo siento, hubo un error"
        const val READY = "Listo"
        const val COMPLETED = "Completado"
        const val UNKNOWN_COMMAND = "Lo siento, no te he entendido"
        const val PERMISSION_NEEDED = "Necesito permisos para esta acción"
        const val NETWORK_ERROR = "Error de conexión"
        const val TRY_AGAIN = "Inténtalo de nuevo"
    }
}