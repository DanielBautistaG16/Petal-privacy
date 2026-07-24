package com.petal.handsfree

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import java.io.File
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.petal.handsfree.shared.SharedInfo

class PetalApplication : Application() {

    companion object {
        private const val TAG = "PetalApplication"
        private const val CRASH_FILE = "petal-crashes.txt"
    }

    override fun attachBaseContext(base: Context) {
        val prefs = base.getSharedPreferences("petal_prefs", Context.MODE_PRIVATE)
        val lang = prefs.getString("app_language", "es") ?: "es"
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration(base.resources.configuration)
        config.setLocale(locale)
        super.attachBaseContext(base.createConfigurationContext(config))
    }

    override fun onCreate() {
        super.onCreate()
        installCrashHandler()
        setupFirebase()
        Log.d(TAG, "Petal Hands-free Application initialized (shared module v${SharedInfo.VERSION})")
    }

    private fun installCrashHandler() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            try {
                // externalFilesDir is readable with any file manager, no adb needed
                val dir = getExternalFilesDir(null) ?: filesDir
                val file = File(dir, CRASH_FILE)
                val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                file.appendText("\n\n=== CRASH $timestamp (thread=${thread.name}) ===\n")
                PrintWriter(file.outputStream().bufferedWriter(), true).use { throwable.printStackTrace(it) }
                Log.e(TAG, "Crash written to ${file.absolutePath}")
            } catch (_: Throwable) {}
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }

    private fun setupFirebase() {
        if (BuildConfig.DEBUG) return
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
    }
}
