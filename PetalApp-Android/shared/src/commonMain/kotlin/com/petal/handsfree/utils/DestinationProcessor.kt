package com.petal.handsfree.utils

/**
 * Pure destination-string processing — no platform dependencies.
 */
object DestinationProcessor {

    // Common location keywords in Spanish
    private val LOCATION_KEYWORDS = mapOf(
        "casa" to "home",
        "trabajo" to "work",
        "oficina" to "office",
        "hospital" to "hospital",
        "farmacia" to "pharmacy",
        "supermercado" to "supermarket",
        "gasolinera" to "gas station",
        "restaurante" to "restaurant",
        "banco" to "bank",
        "centro comercial" to "shopping center"
    )

    private val CITY_HINTS = listOf(
        "valencia", "madrid", "barcelona", "sevilla", "bilbao",
        "españa", "spain", "calle", "avenida", "plaza",
        "centro", "barrio"
    )

    /**
     * Process and enhance destination string.
     *
     * Only translates exact keyword matches (e.g. "casa" → "home") so that
     * partial phrases like "la farmacia" are passed as-is to Maps rather than
     * being mangled into "la pharmacy".  Saved-location shortcuts ("home",
     * "work") must reach Maps without a city suffix — Maps resolves them from
     * the user's saved places.
     */
    fun process(destination: String): String {
        val trimmed = destination.trim()
        val lower = trimmed.lowercase()

        // Exact keyword match → Google Maps saved-location shortcut (no city suffix)
        LOCATION_KEYWORDS[lower]?.let { return it }

        // Named place → add city context for shorter/ambiguous destinations
        return addLocationContext(trimmed)
    }

    /**
     * Add location context for better search results
     */
    private fun addLocationContext(destination: String): String {
        val lowercaseDestination = destination.lowercase()

        // If destination doesn't contain city/location info, add Spanish context
        val hasLocationContext = CITY_HINTS.any { lowercaseDestination.contains(it) }

        return if (!hasLocationContext && destination.length < 20) {
            "$destination Valencia España" // Default to Valencia as per original app
        } else {
            destination
        }
    }
}
