# GUÍA: CONFIGURAR GEMINI API EN PETAL

**Tiempo estimado:** 5-10 minutos
**Costo:** GRATIS (Google ofrece cuota gratuita generosa)

---

## PASO 1: Obtener API Key de Gemini

### 1.1 Ir a Google AI Studio

Abre tu navegador y ve a:
```
https://aistudio.google.com/app/apikey
```

### 1.2 Iniciar sesión

- Usa tu cuenta de Google (cualquiera)
- Si no tienes cuenta, crea una gratis

### 1.3 Crear API Key

1. Click en **"Get API key"** o **"Create API key"**
2. Selecciona un proyecto existente o crea uno nuevo
3. Click en **"Create API key in new project"** (recomendado)
4. Espera unos segundos

### 1.4 Copiar la API Key

Verás algo como:
```
AIzaSyB1234567890abcdefghijklmnopqrstuvwxyz
```

**IMPORTANTE:**
- ✅ Copia la key completa
- ✅ Guárdala en un lugar seguro (la necesitarás)
- ⚠️ NO la compartas públicamente

---

## PASO 2: Configurar la API Key en el Proyecto

### 2.1 Abrir archivo local.properties

Desde tu terminal:

```bash
cd /home/dani/Petal/PetalApp-Android
nano local.properties
```

(O usa tu editor favorito: `code local.properties`, `vim local.properties`, etc.)

### 2.2 Añadir la API Key

Al final del archivo, añade esta línea:

```properties
GEMINI_API_KEY=TU_API_KEY_AQUÍ
```

**Reemplaza `TU_API_KEY_AQUÍ` con la key que copiaste.**

Ejemplo:
```properties
GEMINI_API_KEY=AIzaSyB1234567890abcdefghijklmnopqrstuvwxyz
```

### 2.3 Guardar el archivo

- Si usas nano: `Ctrl+O` → Enter → `Ctrl+X`
- Si usas vim: `Esc` → `:wq` → Enter
- Si usas VS Code: `Ctrl+S`

---

## PASO 3: Verificar la Configuración

Ejecuta este comando para verificar que la key está configurada:

```bash
cd /home/dani/Petal/PetalApp-Android
grep GEMINI_API_KEY local.properties
```

Deberías ver:
```
GEMINI_API_KEY=AIzaSy...
```

Si ves `GEMINI_API_KEY=TU_API_KEY_AQUÍ`, vuelve al Paso 2.2 y reemplaza con tu key real.

---

## PASO 4: Recompilar el APK

Ahora que la API key está configurada, recompila:

```bash
cd /home/dani/Petal/PetalApp-Android
./gradle-8.5/gradle-8.5/bin/gradle clean assembleDebug
```

Espera 1-2 minutos. Deberías ver:
```
BUILD SUCCESSFUL
```

---

## PASO 5: Copiar el Nuevo APK

```bash
cp /home/dani/Petal/PetalApp-Android/app/build/outputs/apk/debug/app-debug.apk \
   /home/dani/Petal/PetalApp-Android/apk-ready/PetalApp-v2.1.1-GEMINI-debug.apk
```

---

## PASO 6: Instalar en tu Móvil

### Opción A: Con ADB (si está conectado)
```bash
adb install -r /home/dani/Petal/PetalApp-Android/apk-ready/PetalApp-v2.1.1-GEMINI-debug.apk
```

### Opción B: Transferencia manual
1. Transfiere el APK a tu móvil
2. Abre el archivo y reinstala la app

---

## PASO 7: Probar las Recomendaciones

Abre Petal → Start Service → Prueba estos comandos:

### ✅ Comandos que DEBERÍAN funcionar ahora:

**Recomendaciones de Lugares:**
```
"Petal, recomiéndame un café"
"Petal, dónde puedo comer"
"Petal, quiero ver algo cultural"
"Petal, un sitio bonito para descansar"
```

**Recomendaciones de Rutas:**
```
"Petal, recomiéndame una ruta turística"
"Petal, qué puedo visitar en bici"
"Petal, una ruta bonita por Valencia"
```

**Respuestas Generales:**
```
"Petal, qué hago ahora"
"Petal, recomiéndame algo"
```

---

## VERIFICAR QUE FUNCIONA

### Test 1: Café
```
Di: "Petal, recomiéndame un café"

Esperas escuchar algo como:
"Café de las Horas. Local histórico, ambiente tranquilo.
 6 minutos. ¿Te llevo?"
```

Si escuchas esto → ✅ ¡FUNCIONA!

Si escuchas "Lo siento, no te he entendido" → ❌ Ver sección TROUBLESHOOTING

---

## TROUBLESHOOTING

### Problema 1: Sigue diciendo "no te he entendido"

**Debug con logcat:**

```bash
adb logcat | grep -E "GeminiClient|VoiceProcessor|Gemini"
```

Prueba un comando y mira el log. Busca:

**Si ves:**
```
GeminiClient: Gemini API key not configured
```
→ La API key NO se configuró. Vuelve al Paso 2.

**Si ves:**
```
GeminiClient: Gemini API error: 400 - API key not valid
```
→ La API key es incorrecta. Verifica que copiaste bien la key.

**Si ves:**
```
GeminiClient: Network error calling Gemini API
```
→ No hay conexión a internet. Conéctate y prueba de nuevo.

**Si ves:**
```
Gemini text content: {"intent":"RECOMMEND_PLACE","argument":"..."}
```
→ ✅ ¡Gemini está respondiendo! El problema está en otro sitio.

### Problema 2: Error 400 - API key not valid

- Verifica que copiaste la key COMPLETA (sin espacios)
- Verifica que es una key de Gemini (no de otro servicio de Google)
- Prueba generar una nueva key en AI Studio

### Problema 3: Error 429 - Quota exceeded

- Has superado el límite gratuito (poco probable)
- Espera unas horas o crea un nuevo proyecto

---

## LÍMITES DE LA API GRATUITA

Google Gemini ofrece:
- **60 requests por minuto** (suficiente para uso personal)
- **1,500 requests por día** (muy generoso)

Para Petal, esto significa:
- Puedes hacer ~1,500 recomendaciones al día
- Suficiente para uso normal de ciclismo

---

## SEGURIDAD: NUNCA SUBAS LA API KEY A GIT

El archivo `local.properties` está en `.gitignore`, así que no se subirá a GitHub.

**NUNCA hagas:**
```bash
git add local.properties  # ❌ NO HAGAS ESTO
```

La API key solo debe estar en tu máquina local.

---

## RESUMEN

1. ✅ Obtener API key: https://aistudio.google.com/app/apikey
2. ✅ Añadir a `local.properties`: `GEMINI_API_KEY=tu_key`
3. ✅ Recompilar APK: `gradle clean assembleDebug`
4. ✅ Instalar en móvil
5. ✅ Probar: "Petal, recomiéndame un café"

---

**¿Listo para empezar?**

Ejecuta los comandos del siguiente mensaje para configurar todo automáticamente.
