# Developer Setup

## Prerequisites

- JDK 17 (`java -version` should show 17.x)
- Android SDK with `compileSdk 36` and `buildTools 34.0.0`
- Android device or emulator running API 26+

## Steps

### 1. Clone and configure secrets

```bash
git clone <repo-url>
cd PetalApp-Android
cp local.properties.example local.properties
```

Edit `local.properties` and fill in:
- `sdk.dir` — path to your Android SDK
- `GEMINI_API_KEY` — from https://aistudio.google.com/app/apikey
- `SPOTIFY_CLIENT_ID` / `SPOTIFY_CLIENT_SECRET` — from Spotify Developer Dashboard (optional for basic testing)

### 2. (Optional) Enable Firebase

1. Create a project at https://console.firebase.google.com
2. Add an Android app with package name `com.petal.handsfree`
3. Download `google-services.json` and place it at `PetalApp-Android/app/google-services.json`
4. In `PetalApp-Android/build.gradle`, uncomment the Firebase plugin lines
5. In `PetalApp-Android/app/build.gradle`, uncomment the two Firebase plugin apply lines and the Firebase dependencies

### 3. Build

```bash
cd PetalApp-Android
./gradlew assembleDebug
# APK at: app/build/outputs/apk/debug/app-debug.apk
```

### 4. Install on device

```bash
./gradlew installDebug
# or: adb install app/build/outputs/apk/debug/app-debug.apk
```

### 5. Copy APK to Windows (WSL users)

```bash
cd ..
bash COPIAR_APK_A_WINDOWS.sh
```

## First run on device

1. Grant all 7 permissions when prompted (microphone and phone call are mandatory)
2. Disable battery optimization when prompted (keeps service alive while cycling)
3. Wait ~3 seconds for the Vosk model to load on first launch
4. Say "Oye Petal" — the indicator should pulse green

## Debug logging

```bash
adb logcat -s VoiceService GeminiClient VoskWakeWord VoiceProcessor
adb logcat -s VoiceService:E GeminiClient:E   # errors only
```

Crash logs (without Firebase) are written to:
`/sdcard/Android/data/com.petal.handsfree.debug/files/petal-crashes.txt`

## Running tests

```bash
./gradlew test                    # all unit tests
./gradlew testDebugUnitTest       # debug variant only
```
