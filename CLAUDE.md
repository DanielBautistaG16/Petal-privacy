# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository Structure

Three sub-projects:

- **`PetalApp-Android/`** — Kotlin Android app ("Petal Handsfree"), a voice-controlled cycling assistant
- **`petal-gps-web/`** — Vanilla HTML/CSS/JS web app; open `index.html` directly in Chrome/Edge 79+ (no build step)
- **`PetalGPS/`** — Legacy B4A (Basic4Android) project; not actively developed

---

## Android App — Build Commands

All commands run from `PetalApp-Android/`:

```bash
./gradlew assembleDebug        # Build debug APK → app/build/outputs/apk/debug/app-debug.apk
./gradlew assembleRelease      # Build release APK (requires signing keys in local.properties)
./gradlew installDebug         # Build and install on connected device
./gradlew test                 # Run unit tests
./gradlew ktlintFormat         # Format Kotlin code
./gradlew clean                # Clean build outputs
```

**`local.properties`** (git-ignored) holds all secrets:
```
GEMINI_API_KEY=your_key_here
SPOTIFY_CLIENT_ID=your_id
SPOTIFY_CLIENT_SECRET=your_secret
RELEASE_STORE_FILE=../keystore/petal-release.jks
RELEASE_STORE_PASSWORD=...
RELEASE_KEY_ALIAS=...
RELEASE_KEY_PASSWORD=...
```

**Build config**: compileSdk 36, minSdk 26, targetSdk 35, Kotlin 1.9.10, AGP 8.1.4, Gradle 8.5.

**Debug logging** via adb:
```bash
adb logcat -s VoiceService GeminiClient VoskWakeWord VoiceProcessor
adb logcat -s VoiceService:E GeminiClient:E   # errors only
adb shell am start -a android.intent.action.CALL -d "tel:611223344"  # test call intent
```

---

## Android App — Architecture

The app uses a **two-tier microphone system**: Vosk holds the mic continuously for wake-word detection; when the wake word fires, Vosk releases the mic so Android's `SpeechRecognizer` can capture the command phrase.

### Data Flow

```
Microphone
    │
    ▼  (always-on, offline)
VoskWakeWordDetector  ──→  wake word matched?
    │                              │
    │  no                          │ yes
    │                              ▼
    │                  trailingCommand present?
    │                      │             │
    │                    yes             no
    │                      │             ▼
    │                      │       Vosk.pause() → SpeechRecognizer
    │                      │             │
    │                      └─────────────┘
    │                              ▼
    │                       VoiceProcessor
    │                    ┌──────────────────┐
    │                    │  offline regex?  │ → ~5 ms, no network
    │                    └──────────────────┘
    │                            │ no match
    │                            ▼
    │                       GeminiClient (gemini-2.0-flash-lite)
    │                            ▼
    │                    {intent, argument}
    │                            ▼
    │              CallHandler / NavigationHandler / TtsHelper / …
    │                            ▼
    │                       TtsHelper → Speaker
    │
    └── (Vosk.resume() after TTS finishes)
```

### Key Design Constraint — Mic Exclusivity

Vosk and Android `SpeechRecognizer` cannot both hold the microphone at the same time. `VoskWakeWordDetector.pause()` must complete before `startCommandCapture()` is called, and `resume()` is called after command processing and TTS finish. Any future microphone user must follow this same protocol.

### Key Components

**`VoskWakeWordDetector`** (`wake/VoskWakeWordDetector.kt`) — Offline wake-word engine backed by Vosk. On first run, extracts the bundled Spanish model from `assets/model-es/` to `filesDir/vosk-model/`, validated with a UUID file to skip re-extraction on subsequent launches. Matches phonetic variants of "Petal" (`pétalo`, `petalo`, `petar`, `peta`, `pedal`) because the word isn't in the Spanish Vosk vocabulary. The `WakeWordDetector` interface (`wake/WakeWordDetector.kt`) abstracts the engine so Vosk could be swapped for Porcupine without touching `VoiceService`.

**`VoiceService`** (`service/VoiceService.kt`) — Core foreground service. Orchestrates the Vosk ↔ SpeechRecognizer mic handoff, manages `TtsHelper` callbacks, and exposes `VoiceState` (LISTENING / WAKE_WORD_DETECTED / PROCESSING) via a static `MutableLiveData`. Also accepts `ACTION_PROCESS_TEXT_COMMAND` intents for the on-screen text input in `MainActivity`.

**`VoiceProcessor`** (`processor/VoiceProcessor.kt`) — Command router. Tries offline regex patterns first (calls, navigation, search, battery, time). Falls back to `GeminiClient` for anything unmatched. Returns `ProcessingResult(intent, argument)`.

**`GeminiClient`** (`client/GeminiClient.kt`) — OkHttp wrapper around `gemini-2.0-flash-lite`. Sends a rigid system prompt that forces `{"intent":"…","argument":"…"}` JSON output. Uses a regex extraction fallback because the model sometimes adds surrounding text despite the prompt. 8-second timeout; returns `null` on 429/network error so `VoiceProcessor` can surface a helpful offline message.

**Handlers** (`utils/`) — Each owns one domain and is instantiated directly by `VoiceProcessor` or `VoiceService`:
- `CallHandler` — phone calls and contact lookup via `Intent.ACTION_CALL`
- `NavigationHandler` — Google Maps bike-mode deep links
- `TtsHelper` — TTS with Spanish locale, exposes a suspend-friendly `speak(text) { onDone }` callback
- `SpotifyHandler` — Spotify via URI scheme
- `RecommendationHandler` — Gemini-powered place/route suggestions
- `IncomingCallHandler` — call detection and voice-based answering
- `AlertCopsHandler` — theft alert integration

**`MainViewModel`** / **`MainActivity`** — Permission handling (7 permissions), service start/stop, animated state indicator observing `VoiceService.currentState`, language toggle (ES/EN via SharedPreferences), and a text-input field that fires `ACTION_PROCESS_TEXT_COMMAND`.

### Intent System

`VoiceProcessor` resolves spoken text to an intent + argument:

| Intent | Trigger examples |
|---|---|
| `CALL` | "Llama al 611223344", "llama a mamá" |
| `NAVIGATE` | "Ir a Casa Carmela", "llévame a la Gran Vía" |
| `SEARCH` | "Recomiéndame un sitio para comer", "busca una farmacia" |
| `SAY` | General Gemini conversational response |
| `BATTERY` | "¿Cuánta batería?" |
| `TIME` | "¿Qué hora es?" |

### Vosk Model Packaging — Critical Build Constraint

Vosk model files must be stored **uncompressed** in the APK or model loading fails. This is configured in `app/build.gradle`:

```groovy
androidResources {
    noCompress += ['mdl', 'fst', 'txt', 'conf', 'int', 'dubm', 'ie', 'mat', 'stats']
}
packaging {
    jniLibs { useLegacyPackaging = true }
}
```

The model lives under `app/src/main/assets/model-es/`. A `uuid` file in that directory drives cache-invalidation on device.

### Async / Lifecycle

- All async work uses Kotlin coroutines scoped to `LifecycleService` with a `SupervisorJob`
- TTS callbacks use a suspend-friendly wrapper in `TtsHelper`
- Wake locks (PARTIAL_WAKE_LOCK) prevent service suspension during processing

### Planned but not yet active (commented-out code)

- Firebase (Firestore, Messaging, Crashlytics, Analytics)
- Room for local persistence
- WhatsApp via AccessibilityService
- Always-on hotword detection via `VoiceInteractionService`

---

## Web App — petal-gps-web

Single-page app with no framework or build step. Open `index.html` in Chrome/Edge 79+ (Web Bluetooth requires Chrome/Edge; Firefox lacks it).

Uses browser APIs directly: Geolocation API (GPS tracking + speed in km/h), Web Bluetooth API (HC-06 / Petal device), MediaRecorder + Web Audio API (audio recording with visualisation). Configuration is persisted to `localStorage`.
