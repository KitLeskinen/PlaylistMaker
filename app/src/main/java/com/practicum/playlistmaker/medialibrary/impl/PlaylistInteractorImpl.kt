package com.practicum.playlistmaker.medialibrary.impl

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

    override fun saveCoverImage(uriString: String): String {
        return playlistRepository.saveCoverImage(uriString)
    }

    override suspend fun getAllPlaylistWithTracks(): List<Playlist> {
        return playlistRepository.getAllPlaylistsWithTracks()
    }

    override suspend fun addTrack(track: Track, playlist: Playlist) : Long {
        return playlistRepository.addTrack(track, playlist)
    }

    override suspend fun removeTrackFromPlaylist(track: Track, playlist: Playlist) {
        playlistRepository.removeTrackFromPlaylist(track, playlist)
    }

    override suspend fun getPlaylistWithTracks(playlistId: Long) : Playlist {
       return playlistRepository.getPlaylistWithTracks(playlistId)
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        playlistRepository.deletePlaylist(playlistId)
    }
}