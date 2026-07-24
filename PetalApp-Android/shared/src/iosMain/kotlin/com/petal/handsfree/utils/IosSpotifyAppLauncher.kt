package com.petal.handsfree.utils

/**
 * TODO(iOS phase): implement via `spotify:` URL scheme / UIApplication.open,
 * with canOpenURL for isInstalled() — mirrors AndroidSpotifyAppLauncher's
 * intent-based approach but iOS has no PackageManager equivalent.
 */
class IosSpotifyAppLauncher : SpotifyAppLauncher {
    override fun isInstalled(): Boolean = false
    override fun openUri(uri: String): Boolean = TODO("spotify: URL scheme / UIApplication.open — iOS phase")
    override fun openSearch(query: String): Boolean = TODO("spotify: URL scheme / UIApplication.open — iOS phase")
    override fun openApp(): Boolean = TODO("spotify: URL scheme / UIApplication.open — iOS phase")
    override fun openWeb(query: String): Boolean = TODO("UIApplication.open(https://open.spotify.com) — iOS phase")
}
