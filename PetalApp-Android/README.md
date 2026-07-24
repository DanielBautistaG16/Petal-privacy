# Petal Handsfree

Voice-controlled cycling assistant for Android. Say **"Oye Petal"** to activate, then give a command — calls, navigation, nearby searches, battery, time.

**Stack**: Kotlin · MVVM · Jetpack LifecycleService · Vosk (offline wake word) · Android SpeechRecognizer · Gemini 2.0 Flash Lite · OkHttp

## Quick start

```bash
cp local.properties.example local.properties   # fill in your API keys
./gradlew assembleDebug
adb install app/build/outputs/apk/debug/app-debug.apk
```

See **[docs/setup.md](../docs/setup.md)** for full setup including Firebase and release signing.

## Docs

| Document | Description |
|----------|-------------|
| [docs/setup.md](../docs/setup.md) | Developer setup from scratch |
| [docs/architecture.md](../docs/architecture.md) | Two-tier mic system, data flow, component responsibilities |
| [docs/voice-commands.md](../docs/voice-commands.md) | All supported voice commands |
| [docs/gemini-prompt.md](../docs/gemini-prompt.md) | Gemini system prompt with tuning notes |

## Build commands

```bash
./gradlew assembleDebug        # debug APK
./gradlew assembleRelease      # release APK (needs signing config in local.properties)
./gradlew installDebug         # build + install on connected device
./gradlew test                 # unit tests
./gradlew ktlintFormat         # format Kotlin
```

## Debug

```bash
adb logcat -s VoiceService GeminiClient VoskWakeWord VoiceProcessor
```

> **Note**: Firebase/Crashlytics requires `app/google-services.json` from Firebase Console.  
> This file is gitignored and must be added manually. See docs/setup.md §2.
