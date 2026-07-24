package com.petal.handsfree.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log

class AndroidMapLauncher(private val context: Context) : MapLauncher {

    companion object {
        private const val TAG = "NavigationHandler"

        private const val GOOGLE_MAPS_PACKAGE = "com.google.android.apps.maps"
        private const val WAZE_PACKAGE = "com.waze"
    }

    override fun isGoogleMapsAvailable(): Boolean {
        return try {
            context.packageManager.getApplicationInfo(GOOGLE_MAPS_PACKAGE, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    override fun isWazeAvailable(): Boolean {
        return try {
            context.packageManager.getApplicationInfo(WAZE_PACKAGE, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    /**
     * Navigate using Google Maps with bike mode.
     *
     * We skip resolveActivity() as a gate because on Android 14+ it can return
     * null even when Maps is installed and handles the scheme (behaviour changed
     * after the package-visibility API hardening in API 34). Catching
     * ActivityNotFoundException is the robust alternative recommended by Google.
     */
    override fun launchGoogleMaps(destination: String): Boolean {
        // Primary: google.navigation: scheme — opens directly in turn-by-turn mode
        return try {
            val navIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("google.navigation:q=${Uri.encode(destination)}&mode=b")
                setPackage(GOOGLE_MAPS_PACKAGE)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(navIntent)
            Log.i(TAG, "Started Google Maps navigation to: $destination")
            true
        } catch (e: android.content.ActivityNotFoundException) {
            // Fallback: geo: URI — Maps opens in search/directions mode
            Log.w(TAG, "google.navigation scheme not handled, falling back to geo: URI")
            try {
                val mapsIntent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("geo:0,0?q=${Uri.encode(destination)}")
                    setPackage(GOOGLE_MAPS_PACKAGE)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(mapsIntent)
                Log.i(TAG, "Fallback: Google Maps search for: $destination")
                true
            } catch (e2: Exception) {
                Log.e(TAG, "Fallback geo: navigation also failed", e2)
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error with Google Maps navigation", e)
            false
        }
    }

    override fun launchWaze(destination: String): Boolean {
        return try {
            val wazeIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("waze://?q=${Uri.encode(destination)}&navigate=yes")
                setPackage(WAZE_PACKAGE)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(wazeIntent)
            Log.i(TAG, "Started Waze navigation to: $destination")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error with Waze navigation", e)
            false
        }
    }

    override fun launchWebMaps(destination: String): Boolean {
        return try {
            val webIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=${Uri.encode(destination)}&travelmode=bicycling")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(webIntent)
            Log.i(TAG, "Started web navigation to: $destination")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error with web navigation", e)
            false
        }
    }

    override fun launchTraffic(): Boolean {
        return try {
            val trafficIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("google.navigation:q=current+location&mode=b&layer=traffic")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            if (isGoogleMapsAvailable()) {
                trafficIntent.setPackage(GOOGLE_MAPS_PACKAGE)
            }
            context.startActivity(trafficIntent)
            Log.i(TAG, "Opened traffic information")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error showing traffic info", e)
            false
        }
    }

    override fun launchCurrentLocation(): Boolean {
        return try {
            val locationIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("geo:0,0?q=current+location")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            if (isGoogleMapsAvailable()) {
                locationIntent.setPackage(GOOGLE_MAPS_PACKAGE)
            }
            context.startActivity(locationIntent)
            Log.i(TAG, "Showing current location")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error showing current location", e)
            false
        }
    }
}
