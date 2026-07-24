package com.petal.handsfree.wake

/**
 * Abstraction over a wake-word detection engine.
 *
 * The engine is expected to listen to the microphone CONTINUOUSLY
 * (no gaps, no auto-stop on silence) and trigger [Listener.onWakeWordDetected]
 * every time the configured hot phrase is detected.
 *
 * After the wake word has been detected the caller will typically call
 * [pause] to release the microphone so a different recognizer (e.g. the
 * platform [android.speech.SpeechRecognizer]) can capture the actual command,
 * and [resume] once that capture has finished.
 *
 * Today this is implemented with Vosk (offline, open-source).
 * In the future we can drop in a Porcupine-backed implementation without
 * touching VoiceService — only the construction site changes.
 */
interface WakeWordDetector {

    interface Listener {
        /**
         * Called from a background thread the moment the engine recognises the wake word.
         *
         * @param matchedPhrase the exact phrase the engine matched (e.g. "oye petal").
         * @param trailingCommand any extra text the engine recognised after the wake
         *                        word in the same utterance, or empty string if none.
         *                        Useful when the user says "oye petal llama a Marta"
         *                        in one breath — we can skip the "Dime" round-trip.
         */
        fun onWakeWordDetected(matchedPhrase: String, trailingCommand: String)

        /** Called when the engine fails to start or crashes mid-listening. */
        fun onError(error: Throwable)
    }

    /**
     * Initialise the engine. Heavy work (model loading, native init) happens here.
     * Must be called on a background thread / coroutine.
     */
    suspend fun initialize()

    /**
     * Start continuous listening. Idempotent — calling it twice is a no-op.
     * Requires [initialize] to have completed successfully.
     */
    suspend fun start(listener: Listener)

    /**
     * Temporarily release the microphone (e.g. while another recognizer runs).
     * The detector keeps its state and can be reactivated quickly with [resume].
     * Suspend because shutdown() can block waiting for the recognizer thread.
     */
    suspend fun pause()

    /**
     * Resume continuous listening after a [pause].
     */
    suspend fun resume()

    /**
     * Stop listening and free the microphone. The detector can still be
     * reactivated later via [start].
     */
    suspend fun stop()

    /**
     * Permanently release all resources (native handles, model, threads).
     * After this call the instance must be discarded.
     */
    suspend fun release()

    /** Whether the engine is currently listening. */
    val isListening: Boolean
}
