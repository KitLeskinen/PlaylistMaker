package com.practicum.playlistmaker.medialibrary.domain

import com.practicum.playlistmaker.common.data.domain.entity.Playlist
import com.practicum.playlistmaker.common.data.domain.entity.Track

interface PlaylistsInteractor {

    suspend fun getPlaylists(): List<Playlist>

    suspend fun savePlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist)

    fun saveCoverImage(uri: String): String

    suspend fun getAllPlaylistWithTracks() : List<Playlist>

    suspend fun addTrack(track: Track, playlist: Playlist) : Long

    suspend fun removeTrackFromPlaylist(track: Track, playlist: Playlist)

    suspend fun getPlaylistWithTracks(playlistId: Long) : Playlist

    suspend fun deletePlaylist(playlistId: Long)
}