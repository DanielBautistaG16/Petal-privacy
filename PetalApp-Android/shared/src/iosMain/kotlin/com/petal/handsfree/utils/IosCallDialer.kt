package com.petal.handsfree.utils

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

/**
 * iOS has no CALL_PHONE-style runtime permission for tel: links — the system
 * shows its own confirmation dialog, so hasCallPermission() is always true.
 */
@Suppress("DEPRECATION")
class IosCallDialer : CallDialer {
    override fun hasCallPermission(): Boolean = true

    override fun dial(cleanNumber: String): Boolean {
        val url = NSURL.URLWithString("tel:$cleanNumber") ?: return false
        val app = UIApplication.sharedApplication
        if (!app.canOpenURL(url)) return false
        return app.openURL(url)
    }

    // Not wired into the voice flow yet on either platform (VoiceProcessor
    // calls makeCall(arg) directly, no name resolution) — Contacts framework
    // access (CNContactStore, permission prompt) is phase-2 work.
    override fun findContactByName(contactName: String): String? = null
}
