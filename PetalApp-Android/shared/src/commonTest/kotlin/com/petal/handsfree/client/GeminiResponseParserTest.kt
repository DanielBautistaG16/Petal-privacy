package com.petal.handsfree.client

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class GeminiResponseParserTest {

    // ─── Valid JSON ───────────────────────────────────────────────────────────

    @Test
    fun cleanCallJsonParsesCorrectly() {
        val result = GeminiResponseParser.parseModelText("""{"intent":"CALL","argument":"611223344"}""")
        assertNotNull(result)
        assertEquals("CALL", result.intent)
        assertEquals("611223344", result.argument)
    }

    @Test
    fun cleanNavigateJsonParsesCorrectly() {
        val result = GeminiResponseParser.parseModelText("""{"intent":"NAVIGATE","argument":"Casa Carmela"}""")
        assertNotNull(result)
        assertEquals("NAVIGATE", result.intent)
        assertEquals("Casa Carmela", result.argument)
    }

    @Test
    fun searchJsonParsesCorrectly() {
        val result = GeminiResponseParser.parseModelText("""{"intent":"SEARCH","argument":"restaurante cerca"}""")
        assertNotNull(result)
        assertEquals("SEARCH", result.intent)
        assertEquals("restaurante cerca", result.argument)
    }

    @Test
    fun sayJsonWithSpanishTextParsesCorrectly() {
        val result = GeminiResponseParser.parseModelText("""{"intent":"SAY","argument":"Hola, estoy aquí para ayudarte."}""")
        assertNotNull(result)
        assertEquals("SAY", result.intent)
        assertEquals("Hola, estoy aquí para ayudarte.", result.argument)
    }

    @Test
    fun batteryJsonWithEmptyArgumentParsesCorrectly() {
        val result = GeminiResponseParser.parseModelText("""{"intent":"BATTERY","argument":""}""")
        assertNotNull(result)
        assertEquals("BATTERY", result.intent)
        assertEquals("", result.argument)
    }

    @Test
    fun timeJsonParsesCorrectly() {
        val result = GeminiResponseParser.parseModelText("""{"intent":"TIME","argument":""}""")
        assertNotNull(result)
        assertEquals("TIME", result.intent)
    }

    // ─── Markdown code fences ─────────────────────────────────────────────────

    @Test
    fun jsonWrappedInMarkdownCodeFencesIsExtractedAndParsed() {
        val modelOutput = "```json\n{\"intent\":\"NAVIGATE\",\"argument\":\"Gran Via Madrid\"}\n```"
        val result = GeminiResponseParser.parseModelText(modelOutput)
        assertNotNull(result)
        assertEquals("NAVIGATE", result.intent)
        assertEquals("Gran Via Madrid", result.argument)
    }

    @Test
    fun jsonWithSurroundingExplanationTextIsExtracted() {
        val modelOutput = "Here is the JSON response: {\"intent\":\"CALL\",\"argument\":\"mamá\"} — done."
        val result = GeminiResponseParser.parseModelText(modelOutput)
        assertNotNull(result)
        assertEquals("CALL", result.intent)
        assertEquals("mamá", result.argument)
    }

    // ─── Error cases ──────────────────────────────────────────────────────────

    @Test
    fun malformedJsonReturnsNullWithoutThrowing() {
        val result = GeminiResponseParser.parseModelText("""{"intent":}""")
        assertNull(result)
    }

    @Test
    fun jsonMissingIntentFieldReturnsNull() {
        val result = GeminiResponseParser.parseModelText("""{"action":"CALL","arg":"611"}""")
        assertNull(result)
    }

    @Test
    fun jsonWithBlankIntentReturnsNull() {
        val result = GeminiResponseParser.parseModelText("""{"intent":"","argument":"test"}""")
        assertNull(result)
    }

    @Test
    fun nullInputReturnsNull() {
        assertNull(GeminiResponseParser.parseModelText(null))
    }

    @Test
    fun emptyStringReturnsNull() {
        assertNull(GeminiResponseParser.parseModelText(""))
    }

    @Test
    fun blankStringReturnsNull() {
        assertNull(GeminiResponseParser.parseModelText("   "))
    }

    @Test
    fun plainTextWithNoJsonReturnsNull() {
        assertNull(GeminiResponseParser.parseModelText("Lo siento, no entendí el comando."))
    }

    // ─── Full API response payload ─────────────────────────────────────────────
    // Regression guard for the Gson→kotlinx.serialization swap: the real Gemini
    // API returns extra top-level fields (usageMetadata, modelVersion, safety
    // ratings inside each candidate) that GeminiApiResponse doesn't model. Gson
    // silently ignored unknown fields by default; kotlinx.serialization does not
    // unless ignoreUnknownKeys is set. This decodes a realistic full response
    // shape end-to-end (not just parseModelText's inner JSON snippet) to make
    // sure that flag is actually wired in.

    @Test
    fun fullApiResponseWithUnknownFieldsDecodesSuccessfully() {
        val fullResponseJson = """
            {
              "candidates": [
                {
                  "content": {
                    "parts": [
                      { "text": "{\"intent\":\"NAVIGATE\",\"argument\":\"Gran Via, Madrid\"}" }
                    ],
                    "role": "model"
                  },
                  "finishReason": "STOP",
                  "index": 0,
                  "safetyRatings": [
                    { "category": "HARM_CATEGORY_HARASSMENT", "probability": "NEGLIGIBLE" },
                    { "category": "HARM_CATEGORY_DANGEROUS_CONTENT", "probability": "NEGLIGIBLE" }
                  ]
                }
              ],
              "usageMetadata": {
                "promptTokenCount": 412,
                "candidatesTokenCount": 14,
                "totalTokenCount": 426
              },
              "modelVersion": "gemini-2.0-flash-lite"
            }
        """.trimIndent()

        val json = Json { ignoreUnknownKeys = true }
        val api = json.decodeFromString<GeminiApiResponse>(fullResponseJson)
        val text = api.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
        assertNotNull(text)

        val result = GeminiResponseParser.parseModelText(text)
        assertNotNull(result)
        assertEquals("NAVIGATE", result.intent)
        assertEquals("Gran Via, Madrid", result.argument)
    }
}
