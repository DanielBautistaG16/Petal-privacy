# PETAL ROUTES & PLACES - MVP SIMPLIFICADO
## Versión de Implementación Rápida

**Versión:** MVP 1.0 (Simplificado)
**Estado:** Listo para implementación
**Fecha:** Enero 2026

---

## OBJETIVO

Implementar recomendaciones inteligentes de **lugares y rutas turísticas** usando solo voz, sin UI compleja.

**Filosofía:**
- Premium, calm, cycling-first
- Voice-first (sin pantallas adicionales por ahora)
- Gemini como cerebro (no necesitamos Google Places API todavía)

---

## LO QUE IMPLEMENTAREMOS

### 1. Recomendaciones de Lugares
Usuario puede preguntar:
- "Petal, recomiéndame un sitio para comer"
- "Petal, dónde puedo tomar un café"
- "Petal, quiero ver algo cultural"
- "Petal, un sitio bonito para descansar"

**Respuesta de Petal:**
- Nombre del lugar
- Descripción breve (1 frase)
- Distancia aproximada
- Pregunta si quiere navegación

**Ejemplo:**
```
Usuario: "Petal, recomiéndame un café"
Petal: "Te recomiendo Dulce de Leche, a 5 minutos.
        Café local, tranquilo. ¿Quieres que te lleve?"
```

### 2. Recomendaciones de Rutas Turísticas
Usuario puede preguntar:
- "Petal, recomiéndame una ruta turística"
- "Petal, qué puedo visitar en bici"
- "Petal, una ruta bonita por Valencia"

**Respuesta de Petal:**
- Nombre de la ruta (generado por Gemini)
- Descripción (2-3 frases)
- Duración estimada
- Puntos destacados (2-3)
- Pregunta si quiere empezar

**Ejemplo:**
```
Usuario: "Petal, una ruta turística"
Petal: "Te propongo una ruta por el centro histórico.
        20 minutos, fácil, por carriles bici.
        Verás la Catedral, Plaza de la Virgen y el Mercado Central.
        ¿Empezamos?"
```

---

## ARQUITECTURA TÉCNICA

### Componentes Nuevos

```
┌─────────────────────────────────────┐
│     VoiceProcessor (existente)      │
│  + 2 nuevos intents                 │
└──────────────┬──────────────────────┘
               │
               ├─ RECOMMEND_PLACE
               │  └─> RecommendationHandler.handlePlace()
               │
               └─ RECOMMEND_ROUTE
                  └─> RecommendationHandler.handleRoute()
```

### Flujo de Datos

```
1. Usuario habla
   ↓
2. VoiceService (reconoce voz)
   ↓
3. VoiceProcessor (detecta intent)
   ↓
4. Si intent = RECOMMEND_PLACE o RECOMMEND_ROUTE:
   ↓
5. GeminiClient (genera recomendación)
   ↓
6. RecommendationHandler (procesa respuesta)
   ↓
7. TtsHelper (habla la recomendación)
   ↓
8. (Opcional) Usuario dice "sí" → NavigationHandler abre Maps
```

---

## IMPLEMENTACIÓN

### 1. Actualizar GeminiClient.kt

**Nuevo System Prompt:**

```kotlin
private val SYSTEM_PROMPT = """
Eres Petal, asistente de voz premium para ciclistas urbanos.

Tu personalidad:
- Calm, premium, confiable
- Conciso (máximo 3 frases)
- Nunca uses lenguaje turístico ("imprescindible", "no te lo pierdas")
- Hablas como un conserje local discreto

Tipos de comandos que entiendes:

1. LLAMADAS
   "llama a 611223344" → {"intent":"CALL","argument":"611223344"}

2. NAVEGACIÓN
   "ir a Casa Carmela" → {"intent":"NAVIGATE","argument":"Casa Carmela Valencia"}

3. RECOMENDACIONES DE LUGARES
   "recomiéndame un café"
   "dónde puedo comer"
   "un sitio cultural"

   → {"intent":"RECOMMEND_PLACE","argument":"descripción del lugar|distancia|¿navegación?"}

   Formato del argument:
   "Dulce de Leche. Café local, tranquilo.|5 minutos|¿Quieres que te lleve?"

4. RECOMENDACIONES DE RUTAS
   "recomiéndame una ruta turística"
   "qué puedo visitar en bici"

   → {"intent":"RECOMMEND_ROUTE","argument":"nombre ruta|descripción|duración|puntos"}

   Formato del argument:
   "Ruta Centro Histórico|Carriles bici por el casco antiguo. Verás Catedral, Plaza Virgen, Mercado Central.|20 minutos|¿Empezamos?"

5. CONVERSACIÓN GENERAL
   "¿cómo está el tráfico?"
   "cuéntame algo"

   → {"intent":"SAY","argument":"respuesta breve y útil"}

Reglas estrictas:
- Máximo 3 frases en cualquier respuesta
- Tono calm, nunca urgente
- Si preguntan sobre Valencia, recomienda lugares reales
- Si preguntan de otra ciudad, usa tu conocimiento pero sé honesto si no sabes
- Distancias aproximadas en minutos de bicicleta
- SIEMPRE responde en JSON válido

Responde SOLO con JSON, sin explicaciones adicionales.
""".trimIndent()
```

### 2. Añadir Nuevos Intents en VoiceProcessor.kt

```kotlin
companion object {
    // Intents existentes
    const val INTENT_CALL = "CALL"
    const val INTENT_CALL_CONTACT = "CALL_CONTACT"
    const val INTENT_NAVIGATE = "NAVIGATE"
    const val INTENT_SAY = "SAY"
    const val INTENT_BATTERY = "BATTERY"
    const val INTENT_TIME = "TIME"

    // NUEVOS INTENTS
    const val INTENT_RECOMMEND_PLACE = "RECOMMEND_PLACE"
    const val INTENT_RECOMMEND_ROUTE = "RECOMMEND_ROUTE"

    const val INTENT_UNKNOWN = "UNKNOWN"
}
```

### 3. Crear RecommendationHandler.kt

```kotlin
package com.petal.handsfree.utils

import android.content.Context
import android.util.Log

/**
 * RecommendationHandler - Maneja recomendaciones de lugares y rutas
 */
class RecommendationHandler(private val context: Context) {

    companion object {
        private const val TAG = "RecommendationHandler"
    }

    private val navigationHandler = NavigationHandler(context)

    /**
     * Maneja recomendación de lugar
     *
     * @param recommendation Formato: "Nombre lugar. Descripción.|Distancia|Pregunta"
     * @return Texto para TTS
     */
    fun handlePlaceRecommendation(recommendation: String): String {
        Log.d(TAG, "Place recommendation: $recommendation")

        return try {
            // Parsear la recomendación (formato: descripción|distancia|pregunta)
            val parts = recommendation.split("|")

            if (parts.size >= 3) {
                val description = parts[0].trim()
                val distance = parts[1].trim()
                val question = parts[2].trim()

                // Respuesta completa
                "$description $distance. $question"
            } else {
                // Formato simple si no viene separado
                recommendation
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing place recommendation", e)
            recommendation
        }
    }

    /**
     * Maneja recomendación de ruta
     *
     * @param recommendation Formato: "Nombre|Descripción|Duración|Pregunta"
     * @return Texto para TTS
     */
    fun handleRouteRecommendation(recommendation: String): String {
        Log.d(TAG, "Route recommendation: $recommendation")

        return try {
            val parts = recommendation.split("|")

            if (parts.size >= 4) {
                val name = parts[0].trim()
                val description = parts[1].trim()
                val duration = parts[2].trim()
                val question = parts[3].trim()

                // Respuesta completa
                "$name. $description $duration. $question"
            } else {
                recommendation
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing route recommendation", e)
            recommendation
        }
    }

    /**
     * Extrae el nombre del lugar de la recomendación para navegación
     */
    fun extractPlaceName(recommendation: String): String? {
        return try {
            // El nombre suele estar al inicio, antes del primer punto
            val firstSentence = recommendation.split("|").firstOrNull() ?: recommendation
            val placeName = firstSentence.split(".").firstOrNull()?.trim()

            // Si encontramos un nombre, devolverlo
            if (!placeName.isNullOrEmpty() && placeName.length > 3) {
                placeName
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error extracting place name", e)
            null
        }
    }
}
```

### 4. Actualizar VoiceProcessor.kt - executeAction()

```kotlin
private suspend fun executeAction(
    intent: String,
    argument: String,
    onResponse: (String) -> Unit
) {
    when (intent) {
        INTENT_CALL -> handleCallAction(argument, onResponse)
        INTENT_CALL_CONTACT -> handleCallContactAction(argument, onResponse)
        INTENT_NAVIGATE -> handleNavigationAction(argument, onResponse)
        INTENT_SAY -> onResponse(argument)
        INTENT_BATTERY -> handleBatteryAction(onResponse)
        INTENT_TIME -> handleTimeAction(onResponse)

        // NUEVOS HANDLERS
        INTENT_RECOMMEND_PLACE -> handleRecommendPlaceAction(argument, onResponse)
        INTENT_RECOMMEND_ROUTE -> handleRecommendRouteAction(argument, onResponse)

        else -> onResponse("Lo siento, no te he entendido")
    }
}

/**
 * Maneja recomendación de lugar
 */
private suspend fun handleRecommendPlaceAction(
    recommendation: String,
    onResponse: (String) -> Unit
) {
    try {
        val recommendationHandler = RecommendationHandler(context)
        val response = recommendationHandler.handlePlaceRecommendation(recommendation)
        onResponse(response)

        // TODO: Guardar última recomendación para posible navegación
        // Si el usuario dice "sí" después, podemos navegar

    } catch (e: Exception) {
        Log.e(TAG, "Error handling place recommendation", e)
        onResponse("Lo siento, no pude procesar la recomendación")
    }
}

/**
 * Maneja recomendación de ruta
 */
private suspend fun handleRecommendRouteAction(
    recommendation: String,
    onResponse: (String) -> Unit
) {
    try {
        val recommendationHandler = RecommendationHandler(context)
        val response = recommendationHandler.handleRouteRecommendation(recommendation)
        onResponse(response)

        // TODO: Guardar última recomendación para posible inicio

    } catch (e: Exception) {
        Log.e(TAG, "Error handling route recommendation", e)
        onResponse("Lo siento, no pude procesar la recomendación")
    }
}
```

---

## EJEMPLOS DE USO

### Caso 1: Recomendación de Café

```
Usuario: "Petal, recomiéndame un café"

[Gemini procesa]

Gemini responde:
{
  "intent": "RECOMMEND_PLACE",
  "argument": "Café de las Horas. Local histórico, ambiente tranquilo.|6 minutos|¿Te llevo?"
}

Petal dice:
"Café de las Horas. Local histórico, ambiente tranquilo. 6 minutos. ¿Te llevo?"

Usuario: "Sí"

[Abre Google Maps en modo bicicleta hacia "Café de las Horas Valencia"]
```

### Caso 2: Ruta Turística

```
Usuario: "Petal, una ruta bonita por el centro"

[Gemini procesa]

Gemini responde:
{
  "intent": "RECOMMEND_ROUTE",
  "argument": "Ruta Jardines del Turia|Carril bici junto al antiguo río. Pasarás por Bioparc, Palau de la Música y Ciutat de les Arts.|35 minutos|¿Quieres empezar?"
}

Petal dice:
"Ruta Jardines del Turia. Carril bici junto al antiguo río.
Pasarás por Bioparc, Palau de la Música y Ciutat de les Arts.
35 minutos. ¿Quieres empezar?"

Usuario: "Vale"

[Abre navegación al primer punto: "Bioparc Valencia"]
```

### Caso 3: Comida

```
Usuario: "Petal, tengo hambre, dónde puedo comer"

Gemini responde:
{
  "intent": "RECOMMEND_PLACE",
  "argument": "Casa Montaña. Tapas tradicionales, terraza agradable.|8 minutos|¿Navegamos?"
}

Petal dice:
"Casa Montaña. Tapas tradicionales, terraza agradable. 8 minutos. ¿Navegamos?"
```

---

## LIMITACIONES DEL MVP

**Lo que NO hace esta versión:**
- ❌ No tiene UI visual con cards (solo voz)
- ❌ No usa Google Places API (Gemini usa su conocimiento)
- ❌ No guarda recomendaciones anteriores
- ❌ No tiene multi-stop routes (solo punto A → B)
- ❌ No personaliza según historial de usuario
- ❌ No valida si los lugares están abiertos (Gemini asume)

**Por qué es suficiente para MVP:**
- ✅ Funciona 100% por voz (hands-free cycling)
- ✅ Gemini tiene conocimiento actualizado de ciudades
- ✅ Rápido de implementar (1-2 días)
- ✅ Valida el concepto antes de invertir en UI compleja
- ✅ Cumple filosofía premium/calm

---

## PRÓXIMOS PASOS (Post-MVP)

**Versión 2.0:**
1. Añadir pantalla simple de recomendaciones (1 card)
2. Botón "Navegar" en vez de confirmación por voz
3. Integrar Google Places API para validar lugares
4. Guardar historial de recomendaciones

**Versión 3.0:**
5. Multi-stop routes (paradas intermedias)
6. Personalización según preferencias
7. Rutas guardadas / favoritas

---

## ARCHIVOS A MODIFICAR

1. **GeminiClient.kt**
   - Actualizar `SYSTEM_PROMPT`
   - No cambiar estructura de request/response

2. **VoiceProcessor.kt**
   - Añadir `INTENT_RECOMMEND_PLACE` y `INTENT_RECOMMEND_ROUTE`
   - Añadir `handleRecommendPlaceAction()` y `handleRecommendRouteAction()`

3. **RecommendationHandler.kt** (NUEVO)
   - Crear clase completa

4. **strings.xml**
   - Añadir strings para errores de recomendaciones

---

## TESTING

**Comandos de prueba:**

```
1. "Petal, recomiéndame un café"
2. "Petal, dónde puedo comer"
3. "Petal, un sitio cultural"
4. "Petal, una ruta turística"
5. "Petal, qué puedo visitar en bici"
6. "Petal, recomiéndame algo bonito"
```

**Validar:**
- ✅ Gemini responde con JSON válido
- ✅ Intent correcto (RECOMMEND_PLACE o RECOMMEND_ROUTE)
- ✅ Petal habla con tono calm
- ✅ Respuesta <15 segundos
- ✅ Si usuario dice "sí", abre Maps

---

## CONCLUSIÓN

Este MVP simplificado permite validar el concepto de recomendaciones inteligentes **sin invertir en UI compleja**.

**Ventajas:**
- Voice-first (perfecto para ciclismo)
- Implementación rápida
- Usa Gemini como cerebro (no necesita múltiples APIs)
- Filosofía premium/calm intacta

**Listo para implementar:** Sí ✅

---

*"Less choice. Better decision."*
– Petal MVP
