import SwiftUI

/// v1 push-to-talk UI: one mic button, no offline wake-word yet (see
/// IosWakeWordDetector.kt phase-2 backlog) — mirrors MainActivity's core
/// loop minus the "Oye Petal" hands-free trigger.
struct ContentView: View {
    @StateObject private var speechManager = SpeechRecognizerManager()
    private let voiceProcessor = VoiceProcessor()
    private let tts = TextToSpeechHelper()

    @State private var statusText = "Toca el micrófono para hablar"
    @State private var responseText = ""
    @State private var isBusy = false

    var body: some View {
        VStack(spacing: 24) {
            Spacer()

            Text("Petal")
                .font(.largeTitle.bold())

            Text(statusText)
                .font(.headline)
                .multilineTextAlignment(.center)
                .padding(.horizontal)

            if !responseText.isEmpty {
                Text(responseText)
                    .font(.body)
                    .foregroundColor(.secondary)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal)
            }

            Spacer()

            Button(action: onMicTapped) {
                Image(systemName: speechManager.isRecording ? "mic.fill" : "mic")
                    .font(.system(size: 48))
                    .foregroundColor(.white)
                    .frame(width: 96, height: 96)
                    .background(speechManager.isRecording ? Color.red : Color.accentColor)
                    .clipShape(Circle())
            }
            .disabled(isBusy)
            .padding(.bottom, 48)
        }
    }

    private func onMicTapped() {
        guard !isBusy, !speechManager.isRecording else { return }

        speechManager.requestAuthorization { granted in
            guard granted else {
                statusText = "Petal necesita permiso de micrófono y de reconocimiento de voz."
                return
            }

            isBusy = true
            statusText = "Te escucho…"
            responseText = ""

            speechManager.startCapture { text in
                DispatchQueue.main.async {
                    guard let text = text, !text.isEmpty else {
                        statusText = "No te he entendido. Inténtalo de nuevo."
                        isBusy = false
                        return
                    }

                    statusText = "Procesando…"
                    voiceProcessor.process(text) { response in
                        DispatchQueue.main.async {
                            responseText = response
                            tts.speak(response) {
                                DispatchQueue.main.async {
                                    statusText = "Toca el micrófono para hablar"
                                    isBusy = false
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

#Preview {
    ContentView()
}
