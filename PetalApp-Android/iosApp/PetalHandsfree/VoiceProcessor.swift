import Foundation
import UIKit
import shared

/// Orchestration layer mirroring app/.../processor/VoiceProcessor.kt on
/// Android: try the offline regex router first, fall back to Gemini, then
/// dispatch to the shared CALL/NAVIGATE handlers or a native BATTERY/TIME
/// reply (Android does these two natively at the app layer too, not in
/// `shared/`, so this class does the same).
///
/// NOTE: `VoiceIntentRouter`, `GeminiClient`, `CallHandler`, `NavigationHandler`
/// and the nested `ProcessingResult`/`GeminiResponse` types below come from the
/// generated `shared` framework — their exact Swift-facing names/signatures
/// are only confirmed once the `ios-build` CI job actually compiles this
/// against the generated Objective-C header (no local Mac to check ahead of
/// time). If CI reports a name mismatch here, adjust to match the generated
/// header — the Kotlin-side logic is already correct and tested.
final class VoiceProcessor {

    private let geminiClient: GeminiClient
    private let callHandler = CallHandler(dialer: IosCallDialer())
    private let navigationHandler = NavigationHandler(launcher: IosMapLauncher())

    init() {
        geminiClient = GeminiClient(
            apiKey: GeneratedSecrets.geminiAPIKey,
            baseUrl: "https://generativelanguage.googleapis.com/v1beta/models/",
            onLog: { message in print("[GeminiClient] \(message)") },
            onNonFatal: { error in print("[GeminiClient] non-fatal: \(error)") }
        )
    }

    /// Routes a recognized command, then calls `onResponse` with the text
    /// Petal should speak back.
    func process(_ spokenText: String, onResponse: @escaping (String) -> Void) {
        let router = VoiceIntentRouter.shared

        if let offline = router.tryOffline(text: spokenText) {
            executeAction(intent: offline.intent, argument: offline.argument, onResponse: onResponse)
            return
        }

        geminiClient.processVoiceCommand(voiceCommand: spokenText) { [weak self] response, _ in
            guard let self = self else { return }
            if let response = response {
                self.executeAction(intent: response.intent, argument: response.argument, onResponse: onResponse)
            } else {
                onResponse("No he podido entenderte. Inténtalo de nuevo.")
            }
        }
    }

    // MARK: - Action execution

    private func executeAction(intent: String, argument: String, onResponse: @escaping (String) -> Void) {
        let router = VoiceIntentRouter.shared
        switch intent {
        case router.INTENT_CALL:
            handleCall(argument, onResponse: onResponse)
        case router.INTENT_NAVIGATE:
            handleNavigate(argument, onResponse: onResponse)
        case router.INTENT_SEARCH:
            handleSearch(argument, onResponse: onResponse)
        case router.INTENT_SAY:
            onResponse(argument)
        case router.INTENT_BATTERY:
            handleBattery(onResponse: onResponse)
        case router.INTENT_TIME:
            handleTime(onResponse: onResponse)
        default:
            onResponse("No he entendido ese comando.")
        }
    }

    private func handleCall(_ argument: String, onResponse: @escaping (String) -> Void) {
        guard !argument.isEmpty else {
            onResponse("¿A quién quieres llamar?")
            return
        }
        if callHandler.makeCall(phoneNumber: argument) {
            onResponse("Llamando a \(argument)")
        } else {
            onResponse("No he podido iniciar la llamada.")
        }
    }

    private func handleNavigate(_ destination: String, onResponse: @escaping (String) -> Void) {
        guard !destination.isEmpty else {
            onResponse("¿A dónde quieres ir?")
            return
        }
        if navigationHandler.navigateToDestination(destination: destination) {
            onResponse("Navegando en bici a \(destination)")
        } else {
            onResponse("No he podido abrir la navegación.")
        }
    }

    // Mirrors Android's handleSearch: a direct Maps search URL, not routed
    // through NavigationHandler (Android doesn't either — see VoiceProcessor.kt).
    private func handleSearch(_ query: String, onResponse: @escaping (String) -> Void) {
        guard !query.isEmpty else {
            onResponse("¿Qué quieres buscar?")
            return
        }
        let encoded = query.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? query
        guard let url = URL(string: "https://www.google.com/maps/search/?api=1&query=\(encoded)") else {
            onResponse("No he podido realizar la búsqueda.")
            return
        }
        UIApplication.shared.open(url)
        onResponse("Buscando \(query)")
    }

    private func handleBattery(onResponse: @escaping (String) -> Void) {
        UIDevice.current.isBatteryMonitoringEnabled = true
        let level = UIDevice.current.batteryLevel
        if level < 0 {
            onResponse("No he podido comprobar la batería.")
        } else {
            onResponse("Tienes un \(Int(level * 100)) por ciento de batería.")
        }
    }

    private func handleTime(onResponse: @escaping (String) -> Void) {
        let formatter = DateFormatter()
        formatter.locale = Locale(identifier: "es_ES")
        formatter.dateFormat = "HH:mm"
        onResponse("Son las \(formatter.string(from: Date()))")
    }
}
