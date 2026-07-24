package com.petal.handsfree.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

/**
 * apiKey/baseUrl/onLog/onNonFatal are injected by the platform composition root
 * (Android: VoiceService, reading BuildConfig + routing to Log/Crashlytics) so
 * this class has no Android or Firebase dependency.
 */
class GeminiClient(
    private val apiKey: String,
    private val baseUrl: String,
    private val httpClient: HttpClient = createHttpClient(),
    private val onLog: (String) -> Unit = {},
    private val onNonFatal: (Throwable) -> Unit = {}
) {

    // Default parameter values aren't exposed across the Kotlin/Native ObjC
    // bridge, so Swift callers (VoiceProcessor.swift) can't omit httpClient —
    // this secondary constructor gives them an explicit entry point that
    // builds the platform HttpClient internally instead.
    constructor(
        apiKey: String,
        baseUrl: String,
        onLog: (String) -> Unit,
        onNonFatal: (Throwable) -> Unit
    ) : this(apiKey, baseUrl, createHttpClient(), onLog, onNonFatal)

    companion object {
        // gemini-2.0-flash-lite: fast, free, low latency — ideal for voice commands
        private const val GEMINI_MODEL = "gemini-2.0-flash-lite"

        private val SYSTEM_PROMPT = """
Eres Petal, el asistente de voz inteligente de una app para ciclistas en España.
Recibes comandos de voz en español y debes devolver SOLO un JSON válido (sin texto adicional):
{"intent":"ACCION","argument":"argumento"}

INTENTS DISPONIBLES:
- CALL: llamadas telefónicas
  argument: número limpio (solo dígitos y +) o nombre del contacto
- NAVIGATE: navegar a un lugar concreto en modo bicicleta
  argument: dirección o nombre del lugar (incluye ciudad si no se menciona, usa España por defecto)
- SEARCH: buscar lugares cercanos o recomendaciones en Google Maps
  argument: término de búsqueda en español (acaba siempre con "cerca" si es búsqueda local)
- SAY: respuesta conversacional que Petal dirá en voz alta
  argument: texto exacto que Petal debe decir
- BATTERY: consultar nivel de batería del móvil (argument: "")
- TIME: consultar la hora actual (argument: "")

REGLAS:
1. Responde SOLO con JSON. Sin markdown, sin explicaciones.
2. Para NAVIGATE: añade ciudad si no está clara.
3. Para SEARCH: siempre acaba el argument con "cerca" para búsquedas locales.
4. Para CALL con nombre: devuelve el nombre exacto que dijo el usuario.
5. Para preguntas generales o conversación: usa SAY.

EJEMPLOS (sigue este formato exactamente):
"llama a mamá" → {"intent":"CALL","argument":"mamá"}
"llama al seis once dos dos tres tres cuatro cuatro" → {"intent":"CALL","argument":"611223344"}
"llama al 611223344" → {"intent":"CALL","argument":"611223344"}
"llévame a casa" → {"intent":"NAVIGATE","argument":"casa"}
"ir al centro comercial" → {"intent":"NAVIGATE","argument":"centro comercial"}
"navega a la calle Gran Vía de Madrid" → {"intent":"NAVIGATE","argument":"Gran Vía, Madrid"}
"recomiéndame un sitio para comer" → {"intent":"SEARCH","argument":"restaurante cerca"}
"busca una farmacia" → {"intent":"SEARCH","argument":"farmacia cerca"}
"quiero tomar un café" → {"intent":"SEARCH","argument":"cafetería cerca"}
"tengo hambre" → {"intent":"SEARCH","argument":"restaurante cerca"}
"dónde puedo comer cerca" → {"intent":"SEARCH","argument":"restaurante cerca"}
"hay algún supermercado" → {"intent":"SEARCH","argument":"supermercado cerca"}
"busco un taller de bicicletas" → {"intent":"SEARCH","argument":"taller bicicletas cerca"}
"quiero comprar agua" → {"intent":"SEARCH","argument":"tienda supermercado cerca"}
"¿cómo está el tiempo?" → {"intent":"SAY","argument":"No tengo acceso al tiempo en este momento, pero puedes comprobarlo en tu app del tiempo."}
"¿cuánta batería me queda?" → {"intent":"BATTERY","argument":""}
"¿qué hora es?" → {"intent":"TIME","argument":""}
"hola" → {"intent":"SAY","argument":"¡Hola! Di un comando como: llévame a, llama a, o recomiéndame algo."}
        """.trimIndent()
    }

    suspend fun processVoiceCommand(voiceCommand: String): GeminiResponse? {
        if (apiKey.isBlank() || apiKey == "ADD_YOUR_KEY_HERE") {
            onLog("Gemini API key not set")
            return null
        }

        return try {
            val url = "$baseUrl$GEMINI_MODEL:generateContent?key=$apiKey"
            val requestBody = GeminiRequest(
                contents = listOf(
                    GeminiContent(parts = listOf(GeminiPart("$SYSTEM_PROMPT\n\nComando de voz: \"$voiceCommand\"")))
                ),
                generationConfig = GeminiGenerationConfig(
                    temperature = 0.1,
                    topK = 1,
                    topP = 0.8,
                    maxOutputTokens = 80,
                    stopSequences = emptyList()
                )
            )

            val response = httpClient.post(url) {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }

            if (response.status.isSuccess()) {
                val api = response.body<GeminiApiResponse>()
                val text = api.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    ?: return null
                onLog("Gemini text: $text")
                GeminiResponseParser.parseModelText(text)
            } else {
                when (response.status.value) {
                    429 -> onLog("Rate limit hit — falling back to offline")
                    401, 403 -> onLog("Invalid API key (${response.status.value})")
                    else -> onLog("API error ${response.status.value}")
                }
                null
            }
        } catch (e: Exception) {
            onLog("Error calling Gemini: ${e.message}")
            onNonFatal(e)
            null
        }
    }
}
