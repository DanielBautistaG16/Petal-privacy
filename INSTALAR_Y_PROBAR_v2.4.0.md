# INSTALACIÓN Y PRUEBA - PETAL v2.4.0

**APK:** `PetalApp-v2.4.0-FULL-REBUILD-debug.apk`
**Tamaño:** 7.4 MB
**Fecha:** 2 Febrero 2026
**Estado:** Recompilación completa desde cero

---

## 🔧 PASO 1: INSTALAR APK

```bash
cd /home/dani/Petal/PetalApp-Android
adb install -r apk-ready/PetalApp-v2.4.0-FULL-REBUILD-debug.apk
```

**Espera a ver:**
```
Performing Streamed Install
Success
```

---

## 🎤 PASO 2: INICIAR SERVICIO

1. Abre la app **Petal** en tu móvil
2. Toca el botón grande **"Start Service"**
3. Verás una notificación permanente: **"Petal Hands-Free Active"**

---

## 🧪 PASO 3: PROBAR COMANDO SIMPLE (sin debug)

Di claramente:

```
"Petal tengo hambre"
```

**Esperado:**
- Debe escucharte (verás el micrófono activarse)
- Puede tardar 2-3 segundos (está llamando a Gemini)
- Debe responderte algo como: "Casa Montaña. Tapas tradicionales. 8 minutos. ¿Te llevo?"

---

## 🔍 PASO 4: SI NO FUNCIONA - DEBUG COMPLETO

### 4.1 Abrir terminal y ejecutar:

```bash
cd /home/dani/Petal
./DEBUG_FULL_v2.4.sh
```

### 4.2 Dejar el terminal corriendo

### 4.3 En el móvil, di:

```
"Petal tengo hambre"
```

### 4.4 Observa la salida del terminal

**Busca estas líneas clave:**

#### ✅ SI FUNCIONA CORRECTAMENTE:

```
GeminiClient: ==== GEMINI REQUEST START ====
GeminiClient: Voice command: 'tengo hambre'
GeminiClient: API Key configured: AIzaSyBLmyDn8E9Ta8TUY...
GeminiClient: Calling Gemini API...
GeminiClient: Response code: 200
GeminiClient: Response message: OK
GeminiClient: Gemini text content: {"intent":"RECOMMEND_PLACE","argument":"..."}
VoiceProcessor: Processed online: RECOMMEND_PLACE
RecommendationHandler: Processing place recommendation: ...
```

#### ❌ SI HAY ERROR - Posibles causas:

**1. No llega a Gemini (procesado offline por error):**
```
VoiceProcessor: Processing voice command: 'tengo hambre'
VoiceProcessor: Processed offline: INTENT_SAY
```
→ **Problema:** Patrón offline interfiere (NO debería pasar)

**2. Gemini no responde o error de red:**
```
GeminiClient: ==== GEMINI REQUEST START ====
GeminiClient: Network error calling Gemini API
```
→ **Problema:** Sin internet o API caída

**3. API key inválida:**
```
GeminiClient: Response code: 400
GeminiClient: Error body: {"error":{"message":"API key not valid"}}
```
→ **Problema:** API key incorrecta o revocada

**4. Quota excedida:**
```
GeminiClient: Response code: 429
GeminiClient: Error body: {"error":{"message":"Quota exceeded"}}
```
→ **Problema:** Límite de requests agotado

**5. No parsea el JSON:**
```
GeminiClient: Response code: 200
GeminiClient: No JSON found in Gemini response
```
→ **Problema:** Gemini no devuelve JSON (prompt no funciona)

---

## 📋 COMANDOS GARANTIZADOS PARA PROBAR

Prueba estos **5 comandos** en orden (con debug activo):

1. **"Petal tengo hambre"**
   → Debe devolver: `RECOMMEND_PLACE`

2. **"Petal necesito un café"**
   → Debe devolver: `RECOMMEND_PLACE`

3. **"Petal recomiéndame una ruta"**
   → Debe devolver: `RECOMMEND_ROUTE`

4. **"Petal dónde estamos"**
   → Debe devolver: `SAY` (con ubicación)

5. **"Petal qué hay por aquí"**
   → Debe devolver: `SAY` (con lugares cercanos)

---

## 🐛 TROUBLESHOOTING

### Problema: Dice "No te he entendido" siempre

**Causas posibles:**
1. No tiene internet → Verifica conexión WiFi/4G
2. API key inválida → Ejecuta debug y verifica Response code
3. Gemini no devuelve JSON → Verifica logs "Gemini text content"

**Solución:**
```bash
# Test directo a API
curl -s -X POST \
  "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=AIzaSyBLmyDn8E9Ta8TUYF-_LayylV96K4jkdtU" \
  -H 'Content-Type: application/json' \
  -d '{"contents":[{"parts":[{"text":"Responde con JSON: {\"intent\":\"RECOMMEND_PLACE\",\"argument\":\"test\"}"}]}]}'
```

Si esto funciona (devuelve JSON), el problema está en la app, no en la API.

---

### Problema: El micrófono no se activa

**Causa:** Servicio no está corriendo

**Solución:**
1. Cierra la app completamente
2. Vuelve a abrirla
3. Start Service
4. Prueba de nuevo

---

### Problema: Se activa el micrófono pero no responde

**Causa:** No reconoce "Petal" al inicio

**Solución:**
- Di "Petal" más CLARO
- Pausa 1 segundo después de "Petal"
- Ejemplo: "Petal... tengo hambre" (con pausa)

---

## ✅ CHECKLIST DE VERIFICACIÓN

Antes de decir "no funciona", verifica:

- [ ] APK v2.4.0 instalado (verifica en la app o con `adb shell pm list packages -f | grep petal`)
- [ ] Servicio iniciado (notificación visible)
- [ ] Internet activo (abre Chrome y navega)
- [ ] Micrófono funciona (prueba con otra app de voz)
- [ ] Dices "Petal" AL INICIO del comando
- [ ] Debug script corriendo ANTES de decir el comando

---

## 📊 REPORTE DE ERROR

Si sigue sin funcionar, **pégame la salida COMPLETA** del DEBUG_FULL_v2.4.sh cuando dices:

```
"Petal tengo hambre"
```

Necesito ver:
1. La línea `==== GEMINI REQUEST START ====`
2. La línea `Response code: XXX`
3. La línea `Response body:` o `Error body:`
4. La línea `Processed online:` o `Processed offline:`

Con eso puedo diagnosticar exactamente qué falla.

---

**¿Todo listo? Instala el APK y prueba con el debug activo.**
