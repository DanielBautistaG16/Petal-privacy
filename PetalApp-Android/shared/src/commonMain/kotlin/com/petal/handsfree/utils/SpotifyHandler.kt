package com.petal.handsfree.utils

import com.petal.handsfree.client.createHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.http.encodeURLParameter
import io.ktor.util.encodeBase64
import kotlin.concurrent.Volatile
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

/**
 * Launches the Spotify app / web fallback. Implemented per-platform because
 * "is Spotify installed" and "open this URI" mean entirely different things
 * on Android (PackageManager + Intent) vs iOS (URL scheme + canOpenURL).
 */
interface SpotifyAppLauncher {
    fun isInstalled(): Boolean
    fun openUri(uri: String): Boolean
    fun openSearch(query: String): Boolean
    fun openApp(): Boolean
    fun openWeb(query: String): Boolean
}

class SpotifyHandler(
    private val clientId: String,
    private val clientSecret: String,
    private val launcher: SpotifyAppLauncher,
    private val httpClient: HttpClient = createHttpClient()
) {

    companion object {
        private const val TOKEN_URL = "https://accounts.spotify.com/api/token"
        private const val SEARCH_URL = "https://api.spotify.com/v1/search"

        @Volatile private var cachedToken: String? = null
        @Volatile private var tokenFetchedAt: TimeMark? = null
        @Volatile private var tokenTtl: Duration = Duration.ZERO
    }

    suspend fun playMusic(query: String): String {
        if (!launcher.isInstalled()) return openWebResult(query)
        if (query.isEmpty()) {
            return if (launcher.openApp()) "Abriendo Spotify" else "No pude abrir Spotify"
        }

        return try {
            val token = getAccessToken() ?: return fallbackToSearch(query)
            val uri = searchSpotifyUri(token, query) ?: return fallbackToSearch(query)
            if (launcher.openUri(uri)) "Reproduciendo $query en Spotify" else fallbackToSearch(query)
        } catch (e: Exception) {
            fallbackToSearch(query)
        }
    }

    // Step 1: Get access token via Client Credentials flow (no user login needed)
    private suspend fun getAccessToken(): String? {
        val cached = cachedToken
        val fetchedAt = tokenFetchedAt
        if (cached != null && fetchedAt != null && fetchedAt.elapsedNow() < tokenTtl) {
            return cached
        }

        return try {
            val credentials = "$clientId:$clientSecret".encodeToByteArray().encodeBase64()

            val response = httpClient.post(TOKEN_URL) {
                header(HttpHeaders.Authorization, "Basic $credentials")
                setBody(FormDataContent(Parameters.build {
                    append("grant_type", "client_credentials")
                }))
            }
            val tokenResponse = response.body<SpotifyTokenResponse>()

            cachedToken = tokenResponse.accessToken
            tokenFetchedAt = TimeSource.Monotonic.markNow()
            // expire 1 minute early to avoid racing the real expiry
            tokenTtl = (tokenResponse.expiresIn.seconds - 60.seconds).coerceAtLeast(0.milliseconds)
            tokenResponse.accessToken
        } catch (e: Exception) {
            null
        }
    }

    // Step 2: Search for the track/playlist URI
    private suspend fun searchSpotifyUri(token: String, query: String): String? {
        return try {
            val isPlaylist = query.lowercase().contains("playlist")
            val type = if (isPlaylist) "playlist" else "track"
            val url = "$SEARCH_URL?q=${query.encodeURLParameter()}&type=$type&limit=1&market=ES"

            val response = httpClient.get(url) {
                header(HttpHeaders.Authorization, "Bearer $token")
            }

            if (isPlaylist) {
                response.body<SpotifyPlaylistSearchResult>().playlists?.items?.firstOrNull()?.uri
            } else {
                response.body<SpotifyTrackSearchResult>().tracks?.items?.firstOrNull()?.uri
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun fallbackToSearch(query: String): String {
        return if (launcher.openSearch(query)) "Buscando $query en Spotify" else openWebResult(query)
    }

    private fun openWebResult(query: String): String {
        return if (launcher.openWeb(query)) {
            if (query.isEmpty()) "Abriendo Spotify en el navegador" else "Buscando $query en Spotify"
        } else {
            "No pude abrir Spotify"
        }
    }
}
