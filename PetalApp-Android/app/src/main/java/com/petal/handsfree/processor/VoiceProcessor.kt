package com.petal.handsfree.processor

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.petal.handsfree.R
import com.petal.handsfree.client.GeminiClient
import com.petal.handsfree.processor.VoiceIntentRouter.tryOffline
import com.petal.handsfree.utils.AndroidCallDialer
import com.petal.handsfree.utils.AndroidMapLauncher
import com.petal.handsfree.utils.CallHandler
import com.petal.handsfree.utils.NavigationHandler
import kotlinx.coroutines.*

class VoiceProcessor(
    private val context: Context,
    private val geminiClient: GeminiClient
) {

    companion object {
        private const val TAG = "VoiceProcessor"

        const val INTENT_CALL = VoiceIntentRouter.INTENT_CALL
        const val INTENT_NAVIGATE = VoiceIntentRouter.INTENT_NAVIGATE
        const val INTENT_SEARCH = VoiceIntentRouter.INTENT_SEARCH
        const val INTENT_SAY = VoiceIntentRouter.INTENT_SAY
        const val INTENT_BATTERY = VoiceIntentRouter.INTENT_BATTERY
        const val INTENT_TIME = VoiceIntentRouter.INTENT_TIME

        private const val GOOGLE_MAPS_PKG = "com.google.android.apps.maps"
    }

    private val callHandler = CallHandler(AndroidCallDialer(context))
    private val navigationHandler = NavigationHandler(AndroidMapLauncher(context))

    suspend fun processVoiceCommand(spokenText: String, onResponse: (String) -> Unit) {
        Log.d(TAG, "Processing: '$spokenText'")

        try {
            val offline = tryOffline(spokenText)
            if (offline != null) {
                Log.d(TAG, "Offline match: ${offline.intent} / '${offline.argument}'")
                executeAction(offline.intent, offline.argument, onResponse)
                return
            }

            Log.d(TAG, "No offline match — calling Gemini")
            val gemini = withContext(Dispatchers.IO) { geminiClient.processVoiceCommand(spokenText) }

            if (gemini != null) {
                Log.d(TAG, "Gemini: ${gemini.intent} / '${gemini.argument}'")
                executeAction(gemini.intent, gemini.argument, onResponse)
            } else {
                onResponse(context.getString(R.string.command_not_recognized_hint))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error processing command", e)
            onResponse(context.getString(R.string.error_processing_command))
        }
    }

    // ─── Action execution ─────────────────────────────────────────────────────

    private suspend fun executeAction(intent: String, argument: String, onResponse: (String) -> Unit) {
        when (intent) {
            INTENT_CALL -> handleCall(argument, onResponse)
            INTENT_NAVIGATE -> handleNavigate(argument, onResponse)
            INTENT_SEARCH -> handleSearch(argument, onResponse)
            INTENT_SAY -> onResponse(argument)
            INTENT_BATTERY -> handleBattery(onResponse)
            INTENT_TIME -> handleTime(onResponse)
            else -> onResponse(context.getString(R.string.command_not_recognized))
        }
    }

    private suspend fun handleCall(arg: String, onResponse: (String) -> Unit) {
        if (arg.isBlank()) {
            onResponse(context.getString(R.string.voice_ask_who_to_call))
            return
        }
        try {
            val success = callHandler.makeCall(arg)
            if (success) onResponse(context.getString(R.string.calling_number, arg))
            else onResponse(context.getString(R.string.error_call_no_permission))
        } catch (e: Exception) {
            Log.e(TAG, "Call error", e)
            onResponse(context.getString(R.string.error_call_generic))
        }
    }

    private suspend fun handleNavigate(destination: String, onResponse: (String) -> Unit) {
        if (destination.isBlank()) {
            onResponse(context.getString(R.string.voice_ask_destination))
            return
        }
        try {
            val success = navigationHandler.navigateToDestination(destination)
            if (success) onResponse(context.getString(R.string.navigating_bike, destination))
            else onResponse(context.getString(R.string.error_navigation_no_app))
        } catch (e: Exception) {
            Log.e(TAG, "Navigation error", e)
            onResponse(context.getString(R.string.error_navigation_failed))
        }
    }

    private fun handleSearch(query: String, onResponse: (String) -> Unit) {
        if (query.isBlank()) {
            onResponse(context.getString(R.string.voice_ask_search_query))
            return
        }
        try {
            val mapsIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("geo:0,0?q=${Uri.encode(query)}")
                setPackage(GOOGLE_MAPS_PKG)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            try {
                context.startActivity(mapsIntent)
                onResponse(context.getString(R.string.searching_in_maps, query))
                return
            } catch (e: android.content.ActivityNotFoundException) {
                Log.w(TAG, "Maps not available for geo: search, falling back to web")
            }
            val webIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://www.google.com/maps/search/${Uri.encode(query)}")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(webIntent)
            onResponse(context.getString(R.string.searching_result, query))
        } catch (e: Exception) {
            Log.e(TAG, "Search error", e)
            onResponse(context.getString(R.string.error_search_failed))
        }
    }

    private fun handleBattery(onResponse: (String) -> Unit) {
        try {
            val bm = context.getSystemService(Context.BATTERY_SERVICE) as android.os.BatteryManager
            val level = bm.getIntProperty(android.os.BatteryManager.BATTERY_PROPERTY_CAPACITY)
            onResponse(context.getString(R.string.battery_level, level))
        } catch (e: Exception) {
            Log.e(TAG, "Battery error", e)
            onResponse(context.getString(R.string.battery_error))
        }
    }

    private fun handleTime(onResponse: (String) -> Unit) {
        try {
            val cal = java.util.Calendar.getInstance()
            val h = cal.get(java.util.Calendar.HOUR_OF_DAY)
            val m = cal.get(java.util.Calendar.MINUTE)
            val time = when {
                m == 0 -> "$h en punto"
                m < 10 -> "$h y cero $m"
                else -> "$h y $m"
            }
            onResponse(context.getString(R.string.current_time, time))
        } catch (e: Exception) {
            Log.e(TAG, "Time error", e)
            onResponse(context.getString(R.string.time_error))
        }
    }
}
