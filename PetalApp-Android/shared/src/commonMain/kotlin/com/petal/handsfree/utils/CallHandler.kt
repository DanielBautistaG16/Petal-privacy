package com.petal.handsfree.utils

/**
 * Placing calls and finding contacts mean entirely different things on
 * Android (permission check + ACTION_CALL Intent + ContentResolver) vs iOS
 * (CallKit / tel: URL scheme + Contacts framework) — implemented per platform.
 */
interface CallDialer {
    fun hasCallPermission(): Boolean
    fun dial(cleanNumber: String): Boolean
    fun findContactByName(contactName: String): String?
}

class CallHandler(private val dialer: CallDialer) {

    /**
     * Make a phone call to the specified number.
     * @return true if call was initiated successfully, false otherwise
     */
    fun makeCall(phoneNumber: String): Boolean {
        if (!dialer.hasCallPermission()) return false

        val cleanNumber = PhoneNumberUtils.clean(phoneNumber)
        if (!PhoneNumberUtils.isValid(cleanNumber)) return false

        return dialer.dial(cleanNumber)
    }

    fun formatPhoneNumberForSpeech(phoneNumber: String): String =
        PhoneNumberUtils.formatForSpeech(phoneNumber)

    fun isEmergencyNumber(phoneNumber: String): Boolean =
        PhoneNumberUtils.isEmergencyNumber(phoneNumber)

    /**
     * Find phone number by contact name.
     * @return The (already cleaned) phone number if found, null otherwise
     */
    fun findContactByName(contactName: String): String? =
        dialer.findContactByName(contactName)

    /**
     * Get call history (future enhancement).
     * Requires READ_CALL_LOG permission — not yet implemented.
     */
    fun getRecentCalls(limit: Int = 5): List<CallRecord> = emptyList()

    data class CallRecord(
        val number: String,
        val timestamp: Long,
        val duration: Long,
        val type: CallType
    )

    enum class CallType {
        INCOMING, OUTGOING, MISSED
    }
}
