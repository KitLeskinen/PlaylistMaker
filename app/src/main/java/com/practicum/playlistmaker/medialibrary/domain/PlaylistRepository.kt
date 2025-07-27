package com.practicum.playlistmaker.medialibrary.domain

import android.net.Uri
import com.practicum.playlistmaker.common.data.domain.entity.Playlist
import com.practicum.playlistmaker.common.data.domain.entity.Track

interface PlaylistRepository {

    suspend fun getPlaylists(): List<Playlist>

    suspend fun savePlaylist(playlist: Playlist)

    fun saveCoverImage(uri: Uri): Uri

    suspend fun getPlaylistWithTracks(playlistId: Long) : Playlist

    suspend fun getAllPlaylistsWithTracks() : List<Playlist>

    suspend fun addTrack(track: Track, playlist: Playlist) : Long

}