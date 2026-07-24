package com.petal.handsfree.utils

/**
 * Launching a maps app means entirely different things on Android (Intent +
 * PackageManager package checks) vs iOS (MKMapItem / URL schemes) —
 * implemented per platform.
 */
interface MapLauncher {
    fun isGoogleMapsAvailable(): Boolean
    fun isWazeAvailable(): Boolean
    fun launchGoogleMaps(destination: String): Boolean
    fun launchWaze(destination: String): Boolean
    fun launchWebMaps(destination: String): Boolean
    fun launchTraffic(): Boolean
    fun launchCurrentLocation(): Boolean
}

class NavigationHandler(private val launcher: MapLauncher) {

    /**
     * Navigate to specified destination using bike mode.
     * @return true if navigation was started successfully, false otherwise
     */
    fun navigateToDestination(destination: String): Boolean {
        val processedDestination = DestinationProcessor.process(destination)

        return when {
            launcher.isGoogleMapsAvailable() -> launcher.launchGoogleMaps(processedDestination)
            launcher.isWazeAvailable() -> launcher.launchWaze(processedDestination)
            else -> launcher.launchWebMaps(processedDestination)
        }
    }

    /**
     * Get available navigation apps
     */
    fun getAvailableNavigationApps(): List<NavigationApp> {
        val availableApps = mutableListOf<NavigationApp>()

        if (launcher.isGoogleMapsAvailable()) availableApps.add(NavigationApp.GOOGLE_MAPS)
        if (launcher.isWazeAvailable()) availableApps.add(NavigationApp.WAZE)

        // Web maps is always available
        availableApps.add(NavigationApp.WEB_MAPS)

        return availableApps
    }

    /**
     * Open the maps app to show traffic information
     */
    fun showTrafficInfo(): Boolean = launcher.launchTraffic()

    /**
     * Navigate to current location (useful for finding where you are)
     */
    fun showCurrentLocation(): Boolean = launcher.launchCurrentLocation()

    enum class NavigationApp(val displayName: String) {
        GOOGLE_MAPS("Google Maps"),
        WAZE("Waze"),
        WEB_MAPS("Maps (Web)")
    }

    data class NavigationResult(
        val success: Boolean,
        val app: NavigationApp,
        val destination: String,
        val error: String? = null
    )
}
