# GitHub Actions Secrets

Configure these secrets in your repository under **Settings → Secrets and variables → Actions**:

| Secret | Description |
|--------|-------------|
| `GEMINI_API_KEY` | Gemini API key from https://aistudio.google.com/app/apikey |
| `SPOTIFY_CLIENT_ID` | Spotify app client ID |
| `SPOTIFY_CLIENT_SECRET` | Spotify app client secret |
| `RELEASE_KEYSTORE_BASE64` | Base64-encoded release keystore (`base64 -i petal-release.jks`) |
| `RELEASE_STORE_PASSWORD` | Keystore password |
| `RELEASE_KEY_ALIAS` | Key alias in keystore |
| `RELEASE_KEY_PASSWORD` | Key password |
| `GOOGLE_SERVICES_JSON` | Contents of `google-services.json` from Firebase Console (for Firebase/Crashlytics) |

## Encoding the keystore

```bash
base64 -i keystore/petal-release.jks | pbcopy   # macOS
base64 keystore/petal-release.jks               # Linux
```

Paste the output as the `RELEASE_KEYSTORE_BASE64` secret.
