# Petal Handsfree — Architecture

## Two-Tier Microphone System

The core design constraint is **mic exclusivity**: Vosk and Android SpeechRecognizer cannot hold the microphone simultaneously. The handoff protocol is strictly:

```
Vosk (always-on) → wake word fires → Vosk.pause() → SpeechRecognizer.start()
                                                              ↓
                                                   command recognized
                                                              ↓
                                               SpeechRecognizer.destroy() → Vosk.resume()
```

If a trailing command follows the wake word in a single breath ("oye petal llama a mamá"), the SpeechRecognizer round-trip is skipped entirely.

## Data Flow

```
Microphone
    │
    ▼  (continuous, offline, always-on)
VoskWakeWordDetector
    │  WAKE_PATTERN: "oye|ey|hey + petal variants"
    │  Phonetic variants: pétalo / petalo / petar / peta / pedal
    │
    ├── trailingCommand present? ──────────────────────────────────────────┐
    │                                                                       │
    │ no trailing command                                                   │ yes
    ▼                                                                       ▼
Vosk.pause() → SpeechRecognizer (command capture)              processCommand(trailingCommand)
                    │ es-ES, QUEUE_ADD, 2s silence
                    ▼
              onResults(text)
                    │
                    ▼
             VoiceProcessor.processVoiceCommand(text)
                    │
                    ├─ tryOffline(text)  ←── ~5 ms, no network
                    │      │
                    │      │ match: regex patterns for CALL / NAVIGATE /
                    │      │        SEARCH / BATTERY / TIME
                    │      │
                    │      └── ProcessingResult(intent, argument)
                    │
                    └─ no match → GeminiClient.processVoiceCommand(text)
                                       │ gemini-2.0-flash-lite
                                       │ 8s timeout
                                       │ JSON: {"intent":"…","argument":"…"}
                                       │
                                       └── GeminiResponse(intent, argument)
                                                  │
                              ┌───────────────────┤
                              │                   │
                           CALL              NAVIGATE          SEARCH
                              │                   │               │
                         CallHandler     NavigationHandler  geo: Maps URI
                              │                   │
                        ACTION_CALL      google.navigation:
                                         q=dest&mode=b
                                                  │
                                         [SAY / BATTERY / TIME handled inline]
                                                  │
                                             TtsHelper.speak()
                                                  │ UtteranceProgressListener.onDone
                                                  │
                                             Vosk.resume()
                                             VoiceState → LISTENING
```

## Component Responsibilities

### VoskWakeWordDetector (`wake/VoskWakeWordDetector.kt`)
- Runs Vosk offline ASR on 16kHz microphone stream 24/7
- Model extracted from `assets/model-es/` to `filesDir/vosk-model/` on first run; UUID-based cache prevents re-extraction
- Fires `Listener.onWakeWordDetected(matchedPhrase, trailingCommand)` on any match
- `pause()` / `resume()` coordinate mic ownership with SpeechRecognizer

### WakeWordDetector interface (`wake/WakeWordDetector.kt`)
- Abstraction that lets Vosk be swapped for Porcupine or another engine without touching VoiceService
- Lifecycle: `initialize()` → `start()` → `pause()` / `resume()` → `release()`

### VoiceService (`service/VoiceService.kt`)
- `LifecycleService` running as foreground service with PARTIAL_WAKE_LOCK
- Owns the Vosk ↔ SpeechRecognizer mic handoff state machine
- Exposes `VoiceState` (LISTENING / WAKE_WORD_DETECTED / PROCESSING) via static `MutableLiveData` so MainActivity can animate the indicator
- Also handles `ACTION_PROCESS_TEXT_COMMAND` for the text input field

### VoiceProcessor (`processor/VoiceProcessor.kt`)
- Offline patterns first, Gemini fallback
- `tryOffline()` is `internal` for unit testing without coroutines
- GeminiClient injected via constructor for testability

### GeminiClient (`client/GeminiClient.kt`)
- OkHttp + Gson, no Retrofit (keep it simple for a single endpoint)
- System prompt forces `{"intent":"…","argument":"…"}` JSON with a regex extraction fallback
- `parseModelText()` companion function is `internal` for unit testing

### Handlers (`utils/`)
Each handler owns a single domain and handles its own error recovery. VoiceProcessor instantiates them directly (no DI framework).

### TtsHelper (`utils/TtsHelper.kt`)
- `pendingUtterances: ConcurrentHashMap` maps utterance IDs to completion lambdas
- `onDone` / `onError` fire on the TTS thread; callers wrap the lambda in `serviceScope.launch`
- Speech rate 0.9×, pitch 1.1× tuned for wind noise while cycling

## Key Invariants

1. **Vosk owns the mic by default.** SpeechRecognizer only gets it inside `startCommandCapture()` after `wakeDetector.pause()` completes.
2. **Every TTS completion calls `wakeDetector.resume()`.** If this is skipped (e.g. on error), the app gets stuck in PROCESSING state. Always test error paths.
3. **`serviceScope` is a `SupervisorJob`.** Failure in one child coroutine doesn't cancel others.
4. **Vosk model assets must be stored uncompressed.** `build.gradle` `noCompress` and `useLegacyPackaging = true` are load-bearing — do not remove.
