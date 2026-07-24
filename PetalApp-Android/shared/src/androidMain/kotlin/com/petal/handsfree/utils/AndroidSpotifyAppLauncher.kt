package com.petal.handsfree.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

class AndroidSpotifyAppLauncher(private val context: Context) : SpotifyAppLauncher {

    companion object {
        private const val SPOTIFY_PACKAGE = "com.spotify.music"
    }

    override fun isInstalled(): Boolean {
        return try {
            context.packageManager.getApplicationInfo(SPOTIFY_PACKAGE, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    // Open Spotify with the exact URI → auto-plays
    override fun openUri(uri: String): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri)).apply {
                setPackage(SPOTIFY_PACKAGE)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun openSearch(query: String): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("spotify:search:${Uri.encode(query)}")
                setPackage(SPOTIFY_PACKAGE)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun openApp(): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_MAIN).apply {
                setPackage(SPOTIFY_PACKAGE)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun openWeb(query: String): Boolean {
        return try {
            val url = if (query.isEmpty()) "https://open.spotify.com"
                      else "https://open.spotify.com/search/${Uri.encode(query)}"
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
            true
        } catch (e: Exception) {
            false
        }
    }
}
