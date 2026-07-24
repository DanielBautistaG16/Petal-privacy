# GUÍA DE DEBUG: GEMINI NO RESPONDE

## 📦 NUEVO APK CON LOGS DETALLADOS

**Archivo:** `PetalApp-v2.1.3-GEMINI-DEBUG-debug.apk`
**Cambios:**
- ✅ Logs MUY detallados de cada request/response
- ✅ Timeout aumentado a 30 segundos
- ✅ Prompt simplificado
- ✅ maxOutputTokens aumentado a 200

---

## 🔧 PASO 1: INSTALAR APK CON DEBUG

```bash
adb install -r /home/dani/Petal/PetalApp-Android/apk-ready/PetalApp-v2.1.3-GEMINI-DEBUG-debug.apk
```

---

## 🔍 PASO 2: EJECUTAR DEBUG EN TIEMPO REAL

### Opción A: Script Automático

```bash
cd /home/dani/Petal
./DEBUG_RECOMMENDATIONS.sh
```

### Opción B: Comando Manual

```bash
adb logcat -c
adb logcat | grep -E "GeminiClient|==== GEMINI|Voice command:|Response code:|Response body:|Error body:"
```

---

## 🎤 PASO 3: PROBAR COMANDO

Abre Petal → Start Service → Di:

```
"Petal recomiéndame un café"
```

(Sin coma después de "Petal")

---

## 📊 PASO 4: ANALIZAR LA SALIDA

### ✅ Si funciona correctamente, verás:

```
GeminiClient: ==== GEMINI REQUEST START ====
GeminiClient: Voice command: 'recomiéndame un café'
GeminiClient: API Key configured: AIzaSyBLmyDn8E9Ta8TUY...
GeminiClient: Request body (first 500 chars): {"contents":[{"parts":[{"text":"Eres Petal..."}]}]...
GeminiClient: Request URL: https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=...
GeminiClient: Calling Gemini API...
GeminiClient: Response code: 200
GeminiClient: Response message: OK
GeminiClient: Response body length: 450
GeminiClient: Response body: {"candidates":[{"content":{"parts":[{"text":"{\"intent\":\"RECOMMEND_PLACE\",\"argument\":\"Café de las Horas. Local histórico.|6 minutos|¿Te llevo?\"}"}]...
GeminiClient: Gemini text content: {"intent":"RECOMMEND_PLACE","argument":"Café de las Horas. Local histórico.|6 minutos|¿Te llevo?"}
VoiceProcessor: Processed online: RECOMMEND_PLACE
RecommendationHandler: Processing place recommendation: Café de las Horas...
```

**→ Todo funciona. Deberías escuchar la recomendación.**

---

### ❌ POSIBLES ERRORES Y SOLUCIONES

#### Error 1: "Gemini API key not configured"

**Log:**
```
GeminiClient: Gemini API key not configured
```

**Causa:** La API key no se incluyó en el build

**Solución:**
```bash
# Verificar que existe en gradle.properties
grep GEMINI_API_KEY /home/dani/Petal/PetalApp-Android/gradle.properties

# Debe mostrar:
# GEMINI_API_KEY=AIzaSyBLmyDn8E9Ta8TUYF-_LayylV96K4jkdtU

# Si no aparece, añadirla y recompilar
```

---

#### Error 2: "Response code: 400" - API key inválida

**Log:**
```
GeminiClient: Response code: 400
GeminiClient: Response message: Bad Request
GeminiClient: Error body: {
  "error": {
    "code": 400,
    "message": "API key not valid. Please pass a valid API key.",
    "status": "INVALID_ARGUMENT"
  }
}
```

**Causa:** La API key es incorrecta o ha sido revocada

**Solución:**
1. Ve a https://aistudio.google.com/app/apikey
2. Verifica que la key existe y está activa
3. Si no, genera una nueva
4. Actualiza `gradle.properties` con la nueva key
5. Recompila

---

#### Error 3: "Response code: 403" - Quota exceeded

**Log:**
```
GeminiClient: Response code: 403
GeminiClient: Error body: {
  "error": {
    "code": 403,
    "message": "Quota exceeded for quota metric...",
    "status": "RESOURCE_EXHAUSTED"
  }
}
```

**Causa:** Has superado el límite gratuito de Gemini

**Soluciones:**
- Espera unas horas (el límite se resetea)
- Crea un nuevo proyecto en Google Cloud y genera nueva API key
- Habilita billing (si quieres usar más de la cuota gratuita)

---

#### Error 4: "Network error calling Gemini API"

**Log:**
```
GeminiClient: Network error calling Gemini API
GeminiClient: Error details: Unable to resolve host...
```

**Causa:** El móvil no tiene internet

**Solución:**
- Verifica que el móvil está conectado a WiFi o datos móviles
- Prueba abrir un navegador para confirmar que internet funciona

---

#### Error 5: Response 200 pero no parsea el JSON

**Log:**
```
GeminiClient: Response code: 200
GeminiClient: Response body: {"candidates":[{"content":{"parts":[{"text":"Aquí está mi recomendación..."}]...
GeminiClient: No JSON found in Gemini response
```

**Causa:** Gemini no está devolviendo JSON válido (está respondiendo en texto plano)

**Solución:** El prompt necesita ser más estricto. Voy a actualizar el prompt.

---

#### Error 6: Sin logs de "GEMINI REQUEST START"

**Causa:** El comando de voz no está llegando a Gemini (se procesa offline o falla antes)

**Verificar:**
```bash
adb logcat | grep "Processing voice command"
```

Busca:
```
VoiceProcessor: Processing voice command: 'recomiéndame un café'
VoiceProcessor: Processed offline: ...
```

Si dice "Processed offline", significa que detectó un patrón offline y nunca llamó a Gemini.

**Solución:** El patrón offline podría estar interfiriendo. Revisar `VoiceProcessor.kt`.

---

## 🧪 TEST RÁPIDO: Verificar API Key Funciona

Ejecuta esto desde tu terminal (Linux/WSL):

```bash
API_KEY="AIzaSyBLmyDn8E9Ta8TUYF-_LayylV96K4jkdtU"

curl -X POST \
  "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$API_KEY" \
  -H 'Content-Type: application/json' \
  -d '{
    "contents": [{
      "parts": [{
        "text": "Recomiéndame un café en Valencia. Responde en JSON: {\"intent\":\"RECOMMEND_PLACE\",\"argument\":\"nombre|descripción\"}"
      }]
    }]
  }'
```

**Si funciona, verás:**
```json
{
  "candidates": [
    {
      "content": {
        "parts": [
          {
            "text": "{\"intent\":\"RECOMMEND_PLACE\",\"argument\":\"Café de las Horas|Histórico, tranquilo\"}"
          }
        ]
      }
    }
  ]
}
```

**Si NO funciona, verás:**
```json
{
  "error": {
    "code": 400,
    "message": "API key not valid..."
  }
}
```

---

## 📋 CHECKLIST DE VERIFICACIÓN

Antes de decir "no funciona", verifica:

- [ ] APK instalado es el v2.1.3-GEMINI-DEBUG
- [ ] Servicio de voz está iniciado (ves la notificación)
- [ ] Móvil tiene internet (prueba abrir un navegador)
- [ ] Dices "Petal" antes del comando
- [ ] Comando es claro: "Petal recomiéndame un café"
- [ ] Logcat está corriendo ANTES de decir el comando
- [ ] API key es correcta en gradle.properties

---

## 🚨 SI NADA FUNCIONA

Pégame la **salida completa** del logcat cuando dices:

```
"Petal recomiéndame un café"
```

Específicamente necesito ver:
1. `==== GEMINI REQUEST START ====`
2. `Response code: XXX`
3. `Response body: ...` o `Error body: ...`

Con eso puedo diagnosticar exactamente qué está fallando.

---

## 🔄 ALTERNATIVA: BACKEND (si API directa sigue sin funcionar)

Si después de todo esto sigue sin funcionar, podemos implementar un backend intermedio (Firebase Functions) como recomienda la guía que compartiste.

**Ventajas:**
- Más seguro (API key no en la app)
- Rate limiting
- Logs centralizados
- Mejor control

**Tiempo:** ~30 minutos de implementación

---

**¿Ejecutaste el debug? Pégame la salida del logcat aquí.**
