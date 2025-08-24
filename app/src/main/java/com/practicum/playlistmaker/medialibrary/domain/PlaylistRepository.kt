package com.practicum.playlistmaker.medialibrary.domain

import com.practicum.playlistmaker.common.data.domain.entity.Playlist
import com.practicum.playlistmaker.common.data.domain.entity.Track

interface PlaylistRepository {

    suspend fun getPlaylists(): List<Playlist>

    suspend fun savePlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist)

    fun saveCoverImage(uriString: String): String

    suspend fun getPlaylistWithTracks(playlistId: Long): Playlist

    suspend fun getAllPlaylistsWithTracks(): List<Playlist>

    suspend fun addTrack(track: Track, playlist: Playlist): Long

    suspend fun removeTrackFromPlaylist(track: Track, playlist: Playlist)

    suspend fun  deletePlaylist(playlistId: Long)

}