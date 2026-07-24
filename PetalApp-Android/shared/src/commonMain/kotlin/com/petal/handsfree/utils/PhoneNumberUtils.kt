package com.petal.handsfree.utils

/**
 * Pure phone-number cleaning/validation/formatting — no platform dependencies.
 */
object PhoneNumberUtils {

    // Phone number validation patterns
    private val PHONE_PATTERNS = listOf(
        Regex("^\\+?[0-9]{7,15}$"), // International format
        Regex("^[0-9]{7,10}$"),      // National format
        Regex("^\\+34[0-9]{9}$"),    // Spain format
        Regex("^[6789][0-9]{8}$")    // Spanish mobile
    )

    /**
     * Clean and normalize phone number
     */
    fun clean(phoneNumber: String): String {
        return phoneNumber
            .replace(Regex("[^0-9+]"), "") // Remove everything except digits and +
            .let { cleaned ->
                // Handle Spanish numbers without country code
                when {
                    cleaned.startsWith("0034") -> "+34${cleaned.drop(4)}"
                    cleaned.startsWith("34") && cleaned.length == 11 -> "+$cleaned"
                    cleaned.matches(Regex("^[6789][0-9]{8}$")) -> "+34$cleaned" // Spanish mobile
                    else -> cleaned
                }
            }
    }

    /**
     * Validate phone number format
     */
    fun isValid(phoneNumber: String): Boolean {
        if (phoneNumber.isEmpty()) return false
        return PHONE_PATTERNS.any { pattern -> pattern.matches(phoneNumber) }
    }

    /**
     * Format phone number for display/speech
     */
    fun formatForSpeech(phoneNumber: String): String {
        val cleaned = clean(phoneNumber)

        return when {
            cleaned.startsWith("+34") -> {
                val number = cleaned.drop(3)
                "${number.take(3)} ${number.drop(3).take(3)} ${number.drop(6)}"
            }
            cleaned.startsWith("+") -> {
                // International format - just add spaces every 3 digits
                cleaned.drop(1).chunked(3).joinToString(" ")
            }
            else -> {
                // National format - group appropriately
                when (cleaned.length) {
                    7 -> "${cleaned.take(3)} ${cleaned.drop(3)}"
                    8 -> "${cleaned.take(4)} ${cleaned.drop(4)}"
                    9 -> "${cleaned.take(3)} ${cleaned.drop(3).take(3)} ${cleaned.drop(6)}"
                    10 -> "${cleaned.take(3)} ${cleaned.drop(3).take(3)} ${cleaned.drop(6)}"
                    else -> cleaned.chunked(3).joinToString(" ")
                }
            }
        }
    }

    /**
     * Check if a phone number appears to be an emergency number
     */
    fun isEmergencyNumber(phoneNumber: String): Boolean {
        val cleaned = clean(phoneNumber)

        val emergencyNumbers = setOf(
            "112", "911", "999", "000", // International
            "+34112", "091", "092", "062", // Spain specific
            "080", "085" // Local emergency
        )

        return emergencyNumbers.contains(cleaned)
    }
}
