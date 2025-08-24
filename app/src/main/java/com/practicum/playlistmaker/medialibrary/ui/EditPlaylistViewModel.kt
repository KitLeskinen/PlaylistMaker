package com.practicum.playlistmaker.medialibrary.ui

import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.common.data.domain.entity.Playlist
import com.practicum.playlistmaker.medialibrary.domain.PlaylistsInteractor
import kotlinx.coroutines.launch

class EditPlaylistViewModel(playlistsInteractor: PlaylistsInteractor) : NewPlaylistViewModel(
    playlistsInteractor
) {

    lateinit var playlist: Playlist

    fun returnPlaylist(): Playlist {
        return playlist
    }



    fun setPlaylistId(id: Long) {

        viewModelScope.launch {
            playlist = playlistsInteractor.getPlaylistWithTracks(id)
            state.value = NewPlayListState.FillingViews(playlist.coverUri, playlist.name, playlist.description)

        }
    }


    override fun savePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistsInteractor.updatePlaylist(playlist)
        }
    }




}