import AVFoundation

/// Thin AVSpeechSynthesizer wrapper mirroring the Android TtsHelper contract:
/// speak(text) with a completion callback fired once the utterance finishes.
final class TextToSpeechHelper: NSObject, AVSpeechSynthesizerDelegate {

    private let synthesizer = AVSpeechSynthesizer()
    private var pendingCompletion: (() -> Void)?

    override init() {
        super.init()
        synthesizer.delegate = self
        try? AVAudioSession.sharedInstance().setCategory(.playback, options: [.duckOthers])
    }

    func speak(_ text: String, onComplete: (() -> Void)? = nil) {
        guard !text.isEmpty else {
            onComplete?()
            return
        }

        let utterance = AVSpeechUtterance(string: text)
        utterance.voice = AVSpeechSynthesisVoice(language: "es-ES")
        utterance.rate = AVSpeechUtteranceDefaultSpeechRate * 0.95
        utterance.pitchMultiplier = 1.05

        pendingCompletion = onComplete
        synthesizer.speak(utterance)
    }

    func stop() {
        synthesizer.stopSpeaking(at: .immediate)
        pendingCompletion = nil
    }

    var isSpeaking: Bool { synthesizer.isSpeaking }

    // MARK: AVSpeechSynthesizerDelegate

    func speechSynthesizer(_ synthesizer: AVSpeechSynthesizer, didFinish utterance: AVSpeechUtterance) {
        let completion = pendingCompletion
        pendingCompletion = nil
        completion?()
    }

    func speechSynthesizer(_ synthesizer: AVSpeechSynthesizer, didCancel utterance: AVSpeechUtterance) {
        let completion = pendingCompletion
        pendingCompletion = nil
        completion?()
    }
}
