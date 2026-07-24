package com.petal.handsfree.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Build
import android.telecom.TelecomManager
import android.telephony.PhoneStateListener
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.util.Log
import android.view.KeyEvent
import androidx.core.content.ContextCompat

/**
 * IncomingCallHandler - Detects and handles incoming phone calls via voice commands.
 *
 * Monitors phone state (ringing, active, idle) and allows the app to
 * answer or reject calls using TelecomManager or a headset hook fallback.
 */
class IncomingCallHandler(
    private val context: Context,
    private val onCallStateChanged: (state: CallState) -> Unit
) {

    enum class CallState { IDLE, RINGING, ACTIVE }

    companion object {
        private const val TAG = "IncomingCallHandler"
    }

    private val telephonyManager =
        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    private val telecomManager =
        context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager

    @Volatile
    var isCallRinging: Boolean = false
        private set

    // Listener for API < 31
    @Suppress("DEPRECATION")
    private val phoneStateListener: PhoneStateListener? =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            object : PhoneStateListener() {
                @Deprecated("Deprecated in Java")
                override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                    handleCallState(state)
                }
            }
        } else null

    // Callback for API >= 31
    private val telephonyCallback: TelephonyCallback? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            object : TelephonyCallback(), TelephonyCallback.CallStateListener {
                override fun onCallStateChanged(state: Int) {
                    handleCallState(state)
                }
            }
        } else null

    fun register() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.w(TAG, "READ_PHONE_STATE permission not granted, skipping registration")
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            telephonyCallback?.let {
                telephonyManager.registerTelephonyCallback(context.mainExecutor, it)
            }
        } else {
            @Suppress("DEPRECATION")
            phoneStateListener?.let {
                telephonyManager.listen(it, PhoneStateListener.LISTEN_CALL_STATE)
            }
        }
        Log.d(TAG, "Registered phone state listener")
    }

    fun unregister() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            telephonyCallback?.let { telephonyManager.unregisterTelephonyCallback(it) }
        } else {
            @Suppress("DEPRECATION")
            phoneStateListener?.let {
                telephonyManager.listen(it, PhoneStateListener.LISTEN_NONE)
            }
        }
        Log.d(TAG, "Unregistered phone state listener")
    }

    private fun handleCallState(state: Int) {
        val newState = when (state) {
            TelephonyManager.CALL_STATE_RINGING -> CallState.RINGING
            TelephonyManager.CALL_STATE_OFFHOOK -> CallState.ACTIVE
            else -> CallState.IDLE
        }

        isCallRinging = newState == CallState.RINGING
        Log.d(TAG, "Call state: $newState")
        onCallStateChanged(newState)
    }

    fun answerCall(): Boolean {
        return try {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ANSWER_PHONE_CALLS)
                == PackageManager.PERMISSION_GRANTED
            ) {
                @Suppress("DEPRECATION")
                telecomManager.acceptRingingCall()
                Log.i(TAG, "Call answered via TelecomManager")
                true
            } else {
                Log.w(TAG, "ANSWER_PHONE_CALLS permission not granted")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error answering call", e)
            false
        }
    }

    fun rejectCall(): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ANSWER_PHONE_CALLS)
                == PackageManager.PERMISSION_GRANTED
            ) {
                telecomManager.endCall()
                Log.i(TAG, "Call rejected via TelecomManager.endCall()")
                true
            } else {
                rejectCallLegacy()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error rejecting call, trying legacy fallback", e)
            rejectCallLegacy()
        }
    }

    // Fallback for API 26-27 or when permission is unavailable
    private fun rejectCallLegacy(): Boolean {
        return try {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.dispatchMediaKeyEvent(
                KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK)
            )
            audioManager.dispatchMediaKeyEvent(
                KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK)
            )
            Log.i(TAG, "Call rejected via headset hook (legacy)")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error rejecting call (legacy)", e)
            false
        }
    }
}
