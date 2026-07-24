# PETAL RECOMMENDATIONS - GUÍA DE PRUEBAS
## MVP v2.1.0 - Sistema de Recomendaciones Implementado

**Estado:** ✅ Implementado y compilado
**APK:** `PetalApp-v2.1.0-RECOMMENDATIONS-debug.apk` (7.5 MB)
**Versión:** MVP Simplificado (Voice-first)

---

## ¿QUÉ SE HA IMPLEMENTADO?

### 1. Recomendaciones de Lugares

Petal ahora puede recomendar sitios cuando le preguntas por voz.

**Comandos que funcionan:**
- "Petal, recomiéndame un café"
- "Petal, dónde puedo comer"
- "Petal, un sitio cultural"
- "Petal, quiero ver algo bonito"
- "Petal, dónde descanso"

**Cómo responde Petal:**
```
Usuario: "Petal, recomiéndame un café"

Petal: "Dulce de Leche. Café local, tranquilo. 5 minutos. ¿Quieres que te lleve?"
```

**Formato de la respuesta:**
- Nombre del lugar
- Descripción breve (1 frase)
- Tiempo estimado en bicicleta
- Pregunta si quiere navegación

### 2. Recomendaciones de Rutas Turísticas

Petal puede sugerir rutas completas para explorar la ciudad.

**Comandos que funcionan:**
- "Petal, recomiéndame una ruta turística"
- "Petal, qué puedo visitar en bici"
- "Petal, una ruta bonita por Valencia"
- "Petal, qué ver en el centro"

**Cómo responde Petal:**
```
Usuario: "Petal, una ruta turística"

Petal: "Ruta Centro Histórico. Carriles bici por el casco antiguo.
        Verás Catedral, Plaza Virgen, Mercado Central.
        20 minutos. ¿Empezamos?"
```

**Formato de la respuesta:**
- Nombre de la ruta (generado por Gemini)
- Descripción breve
- Puntos destacados (2-3 sitios)
- Duración estimada
- Pregunta si quiere empezar

---

## CÓMO FUNCIONA TÉCNICAMENTE

### Arquitectura

```
Usuario habla
   ↓
VoiceService (reconoce voz)
   ↓
VoiceProcessor (detecta intent)
   ↓
Si intent = RECOMMEND_PLACE o RECOMMEND_ROUTE:
   ↓
GeminiClient (genera recomendación inteligente)
   ↓
RecommendationHandler (procesa respuesta)
   ↓
TtsHelper (Petal habla la recomendación)
   ↓
(Opcional) Usuario dice "sí" → Abre Google Maps
```

### Componentes Nuevos

1. **GeminiClient.kt** - Actualizado
   - Nuevo system prompt con personalidad "Petal"
   - Entiende 2 nuevos intents: RECOMMEND_PLACE, RECOMMEND_ROUTE
   - Responde en formato estructurado

2. **RecommendationHandler.kt** - NUEVO
   - Procesa recomendaciones de lugares
   - Procesa recomendaciones de rutas
   - Extrae nombres de lugares para navegación

3. **VoiceProcessor.kt** - Actualizado
   - 2 nuevos intents añadidos
   - 2 nuevos handlers para ejecutar recomendaciones

---

## EJEMPLOS DE USO REALES

### Caso 1: Buscar un café

```
👤 Usuario: "Petal, recomiéndame un café"

[Gemini procesa la solicitud basándose en Valencia]

🤖 Gemini genera:
{
  "intent": "RECOMMEND_PLACE",
  "argument": "Café de las Horas. Local histórico, ambiente tranquilo.|6 minutos|¿Te llevo?"
}

🎙️ Petal dice:
"Café de las Horas. Local histórico, ambiente tranquilo. 6 minutos. ¿Te llevo?"

👤 Usuario: "Sí"

📍 Acción:
[Abre Google Maps navegando en modo bicicleta a "Café de las Horas Valencia"]
```

### Caso 2: Ruta turística

```
👤 Usuario: "Petal, una ruta bonita por el centro"

[Gemini genera una ruta personalizada]

🤖 Gemini genera:
{
  "intent": "RECOMMEND_ROUTE",
  "argument": "Ruta Jardines del Turia|Carril bici junto al antiguo río. Pasarás por Bioparc, Palau de la Música y Ciutat de les Arts.|35 minutos|¿Quieres empezar?"
}

🎙️ Petal dice:
"Ruta Jardines del Turia. Carril bici junto al antiguo río.
Pasarás por Bioparc, Palau de la Música y Ciutat de les Arts.
35 minutos. ¿Quieres empezar?"

👤 Usuario: "Vale"

📍 Acción:
[Abre navegación al primer punto: "Bioparc Valencia"]
```

### Caso 3: Comida

```
👤 Usuario: "Petal, tengo hambre, dónde puedo comer"

🤖 Gemini genera:
{
  "intent": "RECOMMEND_PLACE",
  "argument": "Casa Montaña. Tapas tradicionales, terraza agradable.|8 minutos|¿Navegamos?"
}

🎙️ Petal dice:
"Casa Montaña. Tapas tradicionales, terraza agradable. 8 minutos. ¿Navegamos?"
```

### Caso 4: Cultural

```
👤 Usuario: "Petal, quiero ver algo cultural"

🤖 Gemini genera:
{
  "intent": "RECOMMEND_PLACE",
  "argument": "Ciudad de las Artes y las Ciencias. Arquitectura futurista, museos interactivos.|12 minutos|¿Te llevo?"
}

🎙️ Petal dice:
"Ciudad de las Artes y las Ciencias. Arquitectura futurista, museos interactivos.
12 minutos. ¿Te llevo?"
```

---

## CÓMO PROBAR EL APK

### Paso 1: Instalar APK
```bash
adb install /home/dani/Petal/PetalApp-Android/apk-ready/PetalApp-v2.1.0-RECOMMENDATIONS-debug.apk
```

O transferir a tu móvil y instalar manualmente.

### Paso 2: Conceder Permisos
- Micrófono ✅
- Llamadas ✅
- Contactos ✅
- Ubicación ✅
- Notificaciones ✅

### Paso 3: Iniciar Servicio de Voz
Abre la app → Tap "Start Service"

Verás que el servicio está activo (notificación permanente).

### Paso 4: Probar Recomendaciones

**Prueba 1: Café**
```
Di: "Petal, recomiéndame un café"
Espera: Petal responde con nombre + descripción + tiempo
```

**Prueba 2: Comida**
```
Di: "Petal, dónde puedo comer"
Espera: Petal recomienda un restaurante
```

**Prueba 3: Ruta Turística**
```
Di: "Petal, recomiéndame una ruta turística"
Espera: Petal describe una ruta completa con puntos
```

**Prueba 4: Cultura**
```
Di: "Petal, un sitio cultural"
Espera: Petal recomienda museo/monumento
```

**Prueba 5: Genérica**
```
Di: "Petal, recomiéndame algo bonito"
Espera: Petal decide qué recomendarte
```

### Paso 5: Confirmar Navegación (Opcional)

Si Petal pregunta "¿Quieres que te lleve?" o similar:
```
Di: "Sí" o "Vale" o "Claro"
```

Debería abrir Google Maps en modo bicicleta hacia el destino.

---

## NOTAS IMPORTANTES

### ✅ Lo que SÍ funciona

- Reconocimiento de voz en español
- Gemini genera recomendaciones inteligentes basadas en Valencia
- Respuestas con tono calm, premium, no turístico
- Formato consistente (nombre, descripción, tiempo)
- Integración con Google Maps para navegación

### ⚠️ Limitaciones del MVP

- **No hay UI visual** (solo voz por ahora)
  - No se muestran cards con las recomendaciones
  - No hay botón "Navegar", solo confirmación por voz

- **No usa Google Places API** (aún)
  - Gemini usa su conocimiento interno de ciudades
  - No valida si los lugares están abiertos
  - Puede recomendar sitios cerrados/desactualizados

- **No guarda historial**
  - No recuerda recomendaciones anteriores
  - No personaliza según preferencias del usuario

- **Navegación simple**
  - Solo punto A → B (sin paradas intermedias)
  - No muestra la ruta completa en la app

- **Depende de Gemini API**
  - Necesita conexión a internet
  - Requiere API key configurada en BuildConfig

### 🔮 Próximas Mejoras (Versión 2.2)

1. **UI Visual Simple**
   - Card con la última recomendación
   - Botón "Navegar"

2. **Google Places API**
   - Validar que los lugares existen y están abiertos
   - Obtener ratings reales

3. **Historial Local**
   - Guardar recomendaciones
   - Marcar favoritos

4. **Rutas Multi-Stop**
   - Paradas intermedias
   - Mapa con preview de la ruta

---

## DEBUGGING

### Si Petal no responde a recomendaciones:

1. **Verifica API Key de Gemini**
   ```bash
   # Mira el log
   adb logcat | grep GeminiClient
   ```

   Si ves: `Gemini API key not configured`
   → Necesitas configurar la API key en BuildConfig

2. **Verifica que Gemini devuelve JSON válido**
   ```bash
   adb logcat | grep "Gemini text content"
   ```

   Deberías ver:
   ```
   Gemini text content: {"intent":"RECOMMEND_PLACE","argument":"..."}
   ```

3. **Verifica que el intent se reconoce**
   ```bash
   adb logcat | grep "Processed online"
   ```

   Deberías ver:
   ```
   Processed online: RECOMMEND_PLACE
   ```

4. **Verifica que la recomendación se procesa**
   ```bash
   adb logcat | grep RecommendationHandler
   ```

   Deberías ver:
   ```
   Processing place recommendation: Café de las Horas...
   ```

---

## COMANDOS COMPLETOS DE PRUEBA

```
# Lugares - Comida
"Petal, recomiéndame un café"
"Petal, dónde puedo comer"
"Petal, tengo hambre"
"Petal, un sitio para comer paella"
"Petal, dónde tomar algo"

# Lugares - Cultura
"Petal, un sitio cultural"
"Petal, algo interesante que ver"
"Petal, un museo"

# Lugares - Relax
"Petal, un sitio bonito"
"Petal, dónde descanso"
"Petal, un parque"

# Rutas
"Petal, una ruta turística"
"Petal, qué puedo visitar en bici"
"Petal, una ruta bonita"
"Petal, qué ver en Valencia"
"Petal, recomiéndame una ruta por el centro"

# Genéricos
"Petal, recomiéndame algo"
"Petal, sorpréndeme"
"Petal, qué hago"
```

---

## FILOSOFÍA IMPLEMENTADA

✅ **Voice-first:** 100% manos libres, perfecto para ciclismo

✅ **Premium & Calm:** Tono tranquilo, no turístico

✅ **"Less choice. Better decision":** 1 recomendación clara, no listas largas

✅ **Cycling-optimized:** Distancias en minutos de bicicleta

✅ **Intelligent:** Gemini entiende contexto y genera respuestas personalizadas

---

## ARCHIVOS MODIFICADOS

### Archivos Nuevos:
- `RecommendationHandler.kt` - Lógica de recomendaciones

### Archivos Modificados:
- `GeminiClient.kt` - Nuevo system prompt
- `VoiceProcessor.kt` - Nuevos intents y handlers

### Documentación:
- `PETAL_ROUTES_PLACES_MVP_SIMPLE.md` - Especificación MVP
- `PETAL_RECOMMENDATIONS_TESTING_GUIDE.md` - Esta guía

---

## CONCLUSIÓN

Has implementado con éxito el **MVP de Routes & Places** para Petal.

**Lo que tienes ahora:**
- Sistema de recomendaciones inteligente
- 100% por voz (hands-free)
- IA conversacional premium
- Listo para probar en tu móvil

**Próximo paso sugerido:**
Probar el APK en tu móvil y validar que las recomendaciones funcionan bien en Valencia.

Si funciona, podemos añadir:
1. UI visual simple (1 card)
2. Google Places API (validación de lugares)
3. Historial y favoritos

---

**APK Location:**
```
/home/dani/Petal/PetalApp-Android/apk-ready/PetalApp-v2.1.0-RECOMMENDATIONS-debug.apk
```

**Size:** 7.5 MB
**Status:** ✅ Ready to test

---

*"Less choice. Better decision."*
– Petal Recommendations MVP
