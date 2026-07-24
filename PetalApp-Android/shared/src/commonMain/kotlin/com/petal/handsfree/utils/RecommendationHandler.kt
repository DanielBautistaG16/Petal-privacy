package com.petal.handsfree.utils

/**
 * RecommendationHandler - Handles place and route recommendations
 *
 * Processes AI-generated recommendations and formats them for TTS output.
 * Part of the Petal Routes & Places MVP.
 */
class RecommendationHandler {

    /**
     * Handle place recommendation
     *
     * Format: "Place name. Description.|Distance|Question?"
     * Example: "Dulce de Leche. Local café, quiet.|5 minutes|Want me to navigate?"
     *
     * @param recommendation Raw recommendation from Gemini
     * @return Formatted text for TTS
     */
    fun handlePlaceRecommendation(recommendation: String): String {
        return try {
            // Parse recommendation (format: description|distance|question)
            val parts = recommendation.split("|")

            when {
                parts.size >= 3 -> {
                    val description = parts[0].trim()
                    val distance = parts[1].trim()
                    val question = parts[2].trim()

                    // Complete response
                    "$description $distance. $question"
                }
                parts.size == 2 -> {
                    // Fallback: description + distance
                    val description = parts[0].trim()
                    val distance = parts[1].trim()
                    "$description $distance."
                }
                else -> {
                    // Simple format (no separators)
                    recommendation
                }
            }
        } catch (e: Exception) {
            recommendation
        }
    }

    /**
     * Handle route recommendation
     *
     * Format: "Route name|Description. Highlights.|Duration|Question?"
     * Example: "Old Town Route|Bike lanes through historic center. Cathedral, Plaza, Market.|20 minutes|Shall we start?"
     *
     * @param recommendation Raw recommendation from Gemini
     * @return Formatted text for TTS
     */
    fun handleRouteRecommendation(recommendation: String): String {
        return try {
            val parts = recommendation.split("|")

            when {
                parts.size >= 4 -> {
                    val name = parts[0].trim()
                    val description = parts[1].trim()
                    val duration = parts[2].trim()
                    val question = parts[3].trim()

                    // Complete response
                    "$name. $description $duration. $question"
                }
                parts.size == 3 -> {
                    // Fallback: name + description + duration
                    val name = parts[0].trim()
                    val description = parts[1].trim()
                    val duration = parts[2].trim()
                    "$name. $description $duration."
                }
                else -> {
                    // Simple format (no separators)
                    recommendation
                }
            }
        } catch (e: Exception) {
            recommendation
        }
    }

    /**
     * Extract place name from recommendation for navigation
     *
     * Tries to extract the first meaningful name from the recommendation text
     *
     * @param recommendation Full recommendation text
     * @return Place name if found, null otherwise
     */
    fun extractPlaceName(recommendation: String): String? {
        return try {
            // Place name is usually at the beginning, before the first period
            val firstPart = recommendation.split("|").firstOrNull() ?: recommendation
            val placeName = firstPart.split(".").firstOrNull()?.trim()

            // Return if it looks like a valid place name
            if (!placeName.isNullOrEmpty() && placeName.length > 3) {
                placeName
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Extract route starting point from recommendation for navigation
     *
     * Tries to identify the first destination mentioned in the route
     *
     * @param recommendation Full route recommendation text
     * @return Starting point if found, null otherwise
     */
    fun extractRouteStart(recommendation: String): String? {
        return try {
            // Look for the first mentioned landmark/place in the description
            val parts = recommendation.split("|")

            if (parts.size >= 2) {
                val description = parts[1]

                // Try to find first capitalized word that looks like a place
                // This is a simple heuristic
                val words = description.split(" ", ",", ".")
                val capitalizedWords = words.filter {
                    it.isNotEmpty() &&
                    it[0].isUpperCase() &&
                    it.length > 3 &&
                    !it.matches(Regex("[A-Z]{2,}")) // Not all caps (like "GPS")
                }

                capitalizedWords.firstOrNull()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
