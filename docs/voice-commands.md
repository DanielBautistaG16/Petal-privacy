# Voice Commands Reference

Activation wake word: **"Oye Petal"**, **"Ey Petal"**, **"Hey Petal"**
(Phonetic variants also accepted: pétalo, petalo, petar, peta, pedal)

---

## Offline commands (no internet required, ~5 ms response)

These are matched by regex patterns in `VoiceProcessor.offlinePatterns` before hitting Gemini.

### Calls
- "**Llama a** 611 223 344"
- "**Llama a** María" (searches contacts)
- "**Llama al** 612345678"
- "**Marcar** 611223344"
- "**Telefonea** a..."

### Navigation (opens Google Maps in bike mode)
- "**Ir a** Casa Carmela"
- "**Llévame a** la farmacia"
- "**Navega a** la Gran Vía de Madrid"
- "**Ve a** Valencia Centro"
- "**Pon rumbo a** ..."
- "**Pon el GPS a** ..."

### Search / Recommendations (opens Google Maps geo search)
- "**Recomiéndame** un sitio para comer"
- "**Busca** una farmacia"
- "**Encuentra** un supermercado"
- "**Dónde hay** cafeterías"
- "**Tengo hambre**" → busca restaurante
- "**Quiero comer**" → busca restaurante
- "**Quiero tomar algo**" → busca cafetería

### System
- "**Batería**" / "¿Cuánta batería?" / "Nivel de carga"
- "**Hora**" / "¿Qué hora es?" / "Hora actual"

---

## Gemini-handled commands (requires internet)

Anything not matched offline goes to `gemini-2.0-flash-lite` for intent classification.

### Calls (spoken digits)
- "Llama al seis once dos dos tres tres cuatro cuatro" → converts to number

### Conversational / SAY intent
- "¿Cómo está el tráfico?"
- "¿Qué tiempo hace?"
- "Cuéntame un chiste"
- "Hola"

### Any command not in offline patterns

---

## Intent → Action mapping

| Intent | Action |
|--------|--------|
| `CALL` | `CallHandler.makeCall(arg)` → `Intent.ACTION_CALL tel:NUMBER` |
| `NAVIGATE` | `NavigationHandler.navigateToDestination(arg)` → `google.navigation:q=...&mode=b` |
| `SEARCH` | `geo:0,0?q=...` via Google Maps, web fallback |
| `BATTERY` | `BatteryManager.BATTERY_PROPERTY_CAPACITY` |
| `TIME` | `Calendar.HOUR_OF_DAY` / `Calendar.MINUTE` |
| `SAY` | TTS speaks the `argument` directly |
