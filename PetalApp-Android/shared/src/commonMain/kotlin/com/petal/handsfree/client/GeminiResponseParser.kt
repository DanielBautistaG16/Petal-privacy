package com.petal.handsfree.client

import kotlinx.serialization.json.Json

/**
 * Parses the raw text that Gemini outputs into a GeminiResponse.
 * Pure logic, no HTTP — unit-testable without a real API key.
 */
object GeminiResponseParser {

    // Gemini's real API responses include fields (safetyRatings, usageMetadata, …)
    // this shape doesn't model — ignoreUnknownKeys is required or every real
    // response would fail to parse, unlike Gson's default lenient behavior.
    private val json = Json { ignoreUnknownKeys = true }

    fun parseModelText(text: String?): GeminiResponse? {
        if (text.isNullOrBlank()) return null
        return try {
            // Strip markdown code fences the model sometimes adds despite the prompt
            val cleaned = text.replace(Regex("```json\\s*|\\s*```"), "").trim()
            val jsonSnippet = Regex("""\{[^}]*"intent"[^}]*\}""").find(cleaned)?.value ?: return null
            val result = json.decodeFromString<GeminiResponse>(jsonSnippet)
            if (result.intent.isBlank()) null else result
        } catch (_: Exception) {
            null
        }
    }
}
