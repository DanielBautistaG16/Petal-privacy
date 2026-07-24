package com.petal.handsfree.utils

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpotifyTokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("expires_in") val expiresIn: Int
)

@Serializable
data class SpotifyTrackSearchResult(val tracks: SpotifyTracks? = null)

@Serializable
data class SpotifyTracks(val items: List<SpotifyTrack>? = null)

@Serializable
data class SpotifyTrack(val uri: String)

@Serializable
data class SpotifyPlaylistSearchResult(val playlists: SpotifyPlaylists? = null)

@Serializable
data class SpotifyPlaylists(val items: List<SpotifyPlaylist>? = null)

@Serializable
data class SpotifyPlaylist(val uri: String)
