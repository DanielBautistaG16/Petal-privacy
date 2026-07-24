import Speech
import AVFoundation

/// Push-to-talk speech capture (v1 scope has no offline wake-word — see the
/// iOS phase-2 backlog on IosWakeWordDetector.kt). One tap = one command,
/// mirroring Android's SpeechRecognizer command-capture window.
final class SpeechRecognizerManager: NSObject, ObservableObject {

    @Published private(set) var isRecording = false

    private let recognizer = SFSpeechRecognizer(locale: Locale(identifier: "es-ES"))
    private let audioEngine = AVAudioEngine()
    private var request: SFSpeechAudioBufferRecognitionRequest?
    private var task: SFSpeechRecognitionTask?

    /// Requests microphone + speech-recognition authorization together.
    func requestAuthorization(completion: @escaping (Bool) -> Void) {
        SFSpeechRecognizer.requestAuthorization { speechStatus in
            guard speechStatus == .authorized else {
                DispatchQueue.main.async { completion(false) }
                return
            }
            AVAudioSession.sharedInstance().requestRecordPermission { micGranted in
                DispatchQueue.main.async { completion(micGranted) }
            }
        }
    }

    /// Captures a single spoken command; `onResult` fires exactly once with
    /// the recognized text, or nil if nothing was understood / an error occurred.
    func startCapture(onResult: @escaping (String?) -> Void) {
        guard let recognizer = recognizer, recognizer.isAvailable else {
            onResult(nil)
            return
        }
        if isRecording { stopCapture() }

        let audioSession = AVAudioSession.sharedInstance()
        do {
            try audioSession.setCategory(.record, mode: .measurement, options: .duckOthers)
            try audioSession.setActive(true, options: .notifyOthersOnDeactivation)
        } catch {
            onResult(nil)
            return
        }

        let request = SFSpeechAudioBufferRecognitionRequest()
        request.shouldReportPartialResults = false
        self.request = request

        let inputNode = audioEngine.inputNode
        let recordingFormat = inputNode.outputFormat(forBus: 0)
        inputNode.removeTap(onBus: 0)
        inputNode.installTap(onBus: 0, bufferSize: 1024, format: recordingFormat) { buffer, _ in
            request.append(buffer)
        }

        audioEngine.prepare()
        do {
            try audioEngine.start()
            isRecording = true
        } catch {
            onResult(nil)
            return
        }

        var didFinish = false
        let finishOnce: (String?) -> Void = { [weak self] text in
            guard !didFinish else { return }
            didFinish = true
            self?.stopCapture()
            onResult(text)
        }

        task = recognizer.recognitionTask(with: request) { result, error in
            if let result = result, result.isFinal {
                finishOnce(result.bestTranscription.formattedString)
            } else if error != nil {
                finishOnce(nil)
            }
        }

        // Safety net: Android caps command capture at a few seconds of
        // silence too (SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS) — this
        // guards against SFSpeechRecognizer never reporting `isFinal`.
        DispatchQueue.main.asyncAfter(deadline: .now() + 8) {
            finishOnce(nil)
        }
    }

    func stopCapture() {
        guard isRecording else { return }
        audioEngine.stop()
        audioEngine.inputNode.removeTap(onBus: 0)
        request?.endAudio()
        task?.cancel()
        task = nil
        request = nil
        isRecording = false
        try? AVAudioSession.sharedInstance().setActive(false, options: .notifyOthersOnDeactivation)
    }
}
