package com.petal.handsfree.utils

import platform.Foundation.NSCharacterSet
import platform.Foundation.NSURL
import platform.Foundation.stringByAddingPercentEncodingWithAllowedCharacters
import platform.UIKit.UIApplication

/**
 * URL-scheme based navigation launcher. comgooglemaps:// and waze:// must be
 * declared under LSApplicationQueriesSchemes in Info.plist or canOpenURL()
 * always returns false regardless of whether the app is installed.
 */
@Suppress("DEPRECATION")
class IosMapLauncher : MapLauncher {

    private fun encode(destination: String): String =
        destination.stringByAddingPercentEncodingWithAllowedCharacters(NSCharacterSet.URLQueryAllowedCharacterSet)
            ?: destination

    private fun canOpen(urlString: String): Boolean {
        val url = NSURL.URLWithString(urlString) ?: return false
        return UIApplication.sharedApplication.canOpenURL(url)
    }

    private fun open(urlString: String): Boolean {
        val url = NSURL.URLWithString(urlString) ?: return false
        val app = UIApplication.sharedApplication
        if (!app.canOpenURL(url)) return false
        return app.openURL(url)
    }

    override fun isGoogleMapsAvailable(): Boolean = canOpen("comgooglemaps://")

    override fun isWazeAvailable(): Boolean = canOpen("waze://")

    override fun launchGoogleMaps(destination: String): Boolean =
        open("comgooglemaps://?daddr=${encode(destination)}&directionsmode=bicycling")

    override fun launchWaze(destination: String): Boolean =
        open("waze://?q=${encode(destination)}&navigate=yes")

    override fun launchWebMaps(destination: String): Boolean =
        open("https://www.google.com/maps/dir/?api=1&destination=${encode(destination)}&travelmode=bicycling")

    override fun launchTraffic(): Boolean =
        open("https://www.google.com/maps/@?api=1&map_action=map&layer=traffic")

    override fun launchCurrentLocation(): Boolean =
        open("https://www.google.com/maps/@?api=1&map_action=map")
}
