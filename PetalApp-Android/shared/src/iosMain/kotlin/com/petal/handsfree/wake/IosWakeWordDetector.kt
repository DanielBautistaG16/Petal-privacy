package com.petal.handsfree.wake

/**
 * TODO(iOS phase): implement via Vosk's iOS framework (confirmed to exist)
 * or an alternative on-device wake-word engine, backed by an active
 * AVAudioSession with UIBackgroundModes=audio for background listening.
 */
class IosWakeWordDetector : WakeWordDetector {
    override suspend fun initialize() {
        TODO("Vosk iOS framework / alternate STT engine — iOS phase")
    }

    override suspend fun start(listener: WakeWordDetector.Listener) {
        TODO("Vosk iOS framework / alternate STT engine — iOS phase")
    }

    override suspend fun pause() {
        TODO("Vosk iOS framework / alternate STT engine — iOS phase")
    }

    override suspend fun resume() {
        TODO("Vosk iOS framework / alternate STT engine — iOS phase")
    }

    override suspend fun stop() {
        TODO("Vosk iOS framework / alternate STT engine — iOS phase")
    }

    override suspend fun release() {
        TODO("Vosk iOS framework / alternate STT engine — iOS phase")
    }

    override val isListening: Boolean = false
}
