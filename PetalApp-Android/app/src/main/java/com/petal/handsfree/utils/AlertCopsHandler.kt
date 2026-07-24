package com.petal.handsfree.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log

/**
 * AlertCopsHandler - Manages AlertCops integration
 *
 * Handles:
 * - Opening AlertCops app for stolen bike reports
 * - Fallback to Play Store if app not installed
 * - Quick emergency access
 *
 * @author Petal Team
 * @version 1.0.0
 */
class AlertCopsHandler(private val context: Context) {

    companion object {
        private const val TAG = "AlertCopsHandler"

        // AlertCops app package name (CORRECTO - Ministerio del Interior)
        private const val ALERTCOPS_PACKAGE = "com.alertcops4.app"

        // Play Store URL for AlertCops
        private const val ALERTCOPS_PLAY_STORE_URL = "market://details?id=$ALERTCOPS_PACKAGE"
        private const val ALERTCOPS_PLAY_STORE_WEB = "https://play.google.com/store/apps/details?id=$ALERTCOPS_PACKAGE"
    }

    /**
     * Open AlertCops app for stolen bike report
     *
     * @return true if AlertCops was opened successfully, false otherwise
     */
    fun openAlertCops(): Boolean {
        Log.d(TAG, "Attempting to open AlertCops")

        return try {
            when {
                isAlertCopsInstalled() -> launchAlertCops()
                else -> openPlayStore()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error opening AlertCops", e)
            false
        }
    }

    /**
     * Check if AlertCops is installed
     */
    private fun isAlertCopsInstalled(): Boolean {
        return try {
            context.packageManager.getPackageInfo(ALERTCOPS_PACKAGE, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    /**
     * Launch AlertCops application
     */
    private fun launchAlertCops(): Boolean {
        return try {
            val launchIntent = context.packageManager.getLaunchIntentForPackage(ALERTCOPS_PACKAGE)

            if (launchIntent != null) {
                launchIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(launchIntent)
                Log.i(TAG, "AlertCops launched successfully")
                true
            } else {
                Log.w(TAG, "Could not create launch intent for AlertCops")
                openPlayStore()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error launching AlertCops", e)
            openPlayStore()
        }
    }

    /**
     * Open Play Store to download AlertCops
     */
    private fun openPlayStore(): Boolean {
        return try {
            // Try opening Play Store app first
            val playStoreIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(ALERTCOPS_PLAY_STORE_URL)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

            context.startActivity(playStoreIntent)
            Log.i(TAG, "Opened Play Store for AlertCops")
            true
        } catch (e: Exception) {
            Log.w(TAG, "Could not open Play Store app, trying web version", e)

            // Fallback to web version
            try {
                val webIntent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(ALERTCOPS_PLAY_STORE_WEB)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }

                context.startActivity(webIntent)
                Log.i(TAG, "Opened web Play Store for AlertCops")
                true
            } catch (e2: Exception) {
                Log.e(TAG, "Could not open Play Store at all", e2)
                false
            }
        }
    }

    /**
     * Get AlertCops installation status message
     */
    fun getStatusMessage(): String {
        return if (isAlertCopsInstalled()) {
            "AlertCops instalada - Lista para usar"
        } else {
            "AlertCops no instalada - Se abrirá Play Store"
        }
    }
}
