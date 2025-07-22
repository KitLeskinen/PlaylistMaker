package com.practicum.playlistmaker.medialibrary.impl

import android.net.Uri
import com.practicum.playlistmaker.common.data.domain.entity.Playlist
import com.practicum.playlistmaker.common.data.domain.entity.Track
import com.practicum.playlistmaker.medialibrary.domain.PlaylistRepository
import com.practicum.playlistmaker.medialibrary.domain.PlaylistsInteractor

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistsInteractor {
    override suspend fun getPlaylists(): List<Playlist> {
        return playlistRepository.getPlaylists()
    }

    override suspend fun savePlaylist(playlist: Playlist) {
        playlistRepository.savePlaylist(playlist)
    }

    override fun saveCoverImage(uri: Uri): Uri {
        return playlistRepository.saveCoverImage(uri)
    }

    override suspend fun getAllPlaylistWithTracks(): List<Playlist> {
        return playlistRepository.getAllPlaylistsWithTracks()
    }

    override suspend fun addTrack(track: Track, playlist: Playlist) {
        return playlistRepository.addTrack(track, playlist)
    }
}