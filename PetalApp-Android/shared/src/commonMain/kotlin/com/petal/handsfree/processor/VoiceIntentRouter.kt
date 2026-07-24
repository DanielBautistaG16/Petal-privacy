package com.petal.handsfree.processor

/**
 * Pure offline regex intent router — no platform dependencies.
 * Tried before falling back to Gemini for a network round-trip.
 */
object VoiceIntentRouter {

    const val INTENT_CALL = "CALL"
    const val INTENT_NAVIGATE = "NAVIGATE"
    const val INTENT_SEARCH = "SEARCH"
    const val INTENT_SAY = "SAY"
    const val INTENT_BATTERY = "BATTERY"
    const val INTENT_TIME = "TIME"

    data class ProcessingResult(val intent: String, val argument: String)

    // Fast offline patterns — tried before calling Gemini
    private val offlinePatterns = listOf(
        // Calls: "llama al 611…", "llama a María"
        Regex(
            "(?:llama(?:r)?|marcar?|telefone?a?|llámala|llamada\\s+a)\\s+(?:a|al)?\\s*(.+)",
            setOf(RegexOption.IGNORE_CASE)
        ) to INTENT_CALL,

        // Navigation: "llévame a", "ir a", "navega a", "ve a", "pon rumbo a"
        Regex(
            "(?:llév[ae]me|llev[ae]me|lleva|ir|ve|vamos|navega(?:r)?|abre\\s+ruta|pon\\s+ruta|pon\\s+rumbo|pon\\s+el\\s+gps|direccion)\\s+(?:a|al|hacia|hasta)?\\s*(.+)",
            setOf(RegexOption.IGNORE_CASE)
        ) to INTENT_NAVIGATE,

        // Search/recommendations: "recomiéndame", "busca", "dónde hay"
        Regex(
            "(?:recomié?nda(?:me)?|busca(?:me|r)?|encuentra(?:me)?|dónde\\s+hay|donde\\s+hay|quiero\\s+ir\\s+a|hay\\s+algún|hay\\s+alguna|sitio\\s+para|lugares?\\s+para)\\s+(.+)",
            setOf(RegexOption.IGNORE_CASE)
        ) to INTENT_SEARCH,

        // Food/drinks shortcuts: "quiero comer", "tengo hambre", "quiero tomar algo"
        Regex(
            "(?:quiero|necesito|tengo)\\s+(?:comer|hambre|beber|tomar\\s+algo|desayunar|cenar|algo\\s+de\\s+comer|algo\\s+de\\s+beber)",
            setOf(RegexOption.IGNORE_CASE)
        ) to INTENT_SEARCH,

        // Battery
        Regex(
            "(?:batería|bateria|carga|nivel\\s+de\\s+batería|cuánta\\s+batería|porcentaje\\s+de\\s+carga)",
            setOf(RegexOption.IGNORE_CASE)
        ) to INTENT_BATTERY,

        // Time
        Regex(
            "(?:qué\\s+hora|que\\s+hora|hora\\s+es|hora\\s+actual|dime\\s+la\\s+hora)",
            setOf(RegexOption.IGNORE_CASE)
        ) to INTENT_TIME
    )

    fun tryOffline(text: String): ProcessingResult? {
        for ((pattern, intent) in offlinePatterns) {
            val m = pattern.find(text)
            if (m != null) {
                val group1 = m.groupValues.getOrNull(1)?.trim() ?: ""
                val arg = when (intent) {
                    INTENT_CALL -> {
                        val digits = group1.replace(Regex("[^0-9+]"), "")
                        if (digits.length >= 7) digits else group1
                    }
                    INTENT_NAVIGATE -> group1
                    INTENT_SEARCH -> buildSearchQuery(text, group1)
                    INTENT_BATTERY, INTENT_TIME -> ""
                    else -> group1
                }
                return ProcessingResult(intent, arg)
            }
        }
        return null
    }

    private fun buildSearchQuery(fullText: String, rawArg: String): String {
        val lower = fullText.lowercase()
        return when {
            rawArg.isNotBlank() -> "$rawArg cerca"
            lower.contains("comer") || lower.contains("hambre") || lower.contains("restaurante") -> "restaurante cerca"
            lower.contains("beber") || lower.contains("tomar") || lower.contains("café") -> "cafetería bar cerca"
            lower.contains("desayunar") -> "desayuno cafetería cerca"
            lower.contains("cenar") -> "restaurante cena cerca"
            lower.contains("farmacia") -> "farmacia cerca"
            lower.contains("supermercado") || lower.contains("comprar") -> "supermercado cerca"
            else -> rawArg.ifBlank { "lugar de interés cerca" }
        }
    }
}
