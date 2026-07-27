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
| `APPLE_TEAM_ID` | Your 10-character Apple Developer Team ID (developer.apple.com → Membership) |
| `IOS_DIST_CERT_P12_BASE64` | Base64-encoded Apple Distribution certificate + private key, exported as `.p12` |
| `IOS_DIST_CERT_PASSWORD` | Password you set when exporting the `.p12` |
| `IOS_PROVISIONING_PROFILE_BASE64` | Base64-encoded App Store provisioning profile (`.mobileprovision`) for `com.petal.handsfree` |
| `APP_STORE_CONNECT_KEY_ID` | Key ID of an App Store Connect API key (Users and Access → Integrations → App Store Connect API) |
| `APP_STORE_CONNECT_ISSUER_ID` | Issuer ID shown on the same API Keys page |
| `APP_STORE_CONNECT_KEY_P8_BASE64` | Base64-encoded contents of the downloaded `AuthKey_<KEY_ID>.p8` file (Apple only lets you download this once — save the original) |

## Encoding the keystore

```bash
base64 -i keystore/petal-release.jks | pbcopy   # macOS
base64 keystore/petal-release.jks               # Linux
```

Paste the output as the `RELEASE_KEYSTORE_BASE64` secret.

## Getting the iOS signing secrets

These all require an approved Apple Developer Program membership first.

1. **Distribution certificate (`IOS_DIST_CERT_P12_BASE64` / `IOS_DIST_CERT_PASSWORD`)**
   - Generate a CSR — works fine without a Mac:
     `openssl req -new -newkey rsa:2048 -nodes -keyout dist.key -out dist.csr -subj "/CN=Petal Handsfree/emailAddress=you@example.com"`
   - developer.apple.com → Certificates → + → **Apple Distribution** → upload `dist.csr` → download the resulting `.cer`
   - Convert to `.p12` (needs the `.key` from the CSR step):
     `openssl x509 -in distribution.cer -inform DER -out dist.pem -outform PEM`
     `openssl pkcs12 -export -inkey dist.key -in dist.pem -out dist.p12 -password pass:<choose-a-password>`
   - `base64 -w0 dist.p12` (Linux) or `base64 -i dist.p12` (macOS) → `IOS_DIST_CERT_P12_BASE64`; the password you chose → `IOS_DIST_CERT_PASSWORD`
2. **App ID + provisioning profile (`IOS_PROVISIONING_PROFILE_BASE64`)**
   - developer.apple.com → Identifiers → + → App ID `com.petal.handsfree`
   - developer.apple.com → Profiles → + → **App Store** distribution type → select the App ID and the certificate from step 1 → download the `.mobileprovision`
   - `base64 -w0 PetalHandsfree.mobileprovision` → `IOS_PROVISIONING_PROFILE_BASE64`
3. **App Store Connect API key (`APP_STORE_CONNECT_KEY_ID` / `APP_STORE_CONNECT_ISSUER_ID` / `APP_STORE_CONNECT_KEY_P8_BASE64`)**
   - appstoreconnect.apple.com → Users and Access → Integrations → App Store Connect API → Generate API Key (Admin or App Manager role)
   - Note the Key ID and Issuer ID shown on that page
   - Download the `.p8` immediately — Apple only offers it once — then `base64 -w0 AuthKey_<KEY_ID>.p8` → `APP_STORE_CONNECT_KEY_P8_BASE64`
4. Also create the app record itself in App Store Connect (My Apps → +) with bundle ID `com.petal.handsfree` before the first TestFlight upload will be accepted.
