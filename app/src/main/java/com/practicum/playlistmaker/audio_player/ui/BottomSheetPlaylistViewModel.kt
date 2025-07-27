package com.practicum.playlistmaker.audio_player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.common.data.domain.entity.Playlist
import com.practicum.playlistmaker.common.data.domain.entity.Track
import com.practicum.playlistmaker.medialibrary.domain.PlaylistsInteractor
import kotlinx.coroutines.launch

class BottomSheetPlaylistViewModel(private val playlistsInteractor: PlaylistsInteractor) :
    ViewModel() {

    private val _state = MutableLiveData<BottomSheetPlaylistState>()
    val state: LiveData<BottomSheetPlaylistState>  = _state


    fun updatePlaylistItems() {
        loadPlaylistItems()
    }

    fun addTrackToPlayList(selectedTrack: Track, playlist: Playlist) {
        viewModelScope.launch {
            val result = playlistsInteractor.addTrack(selectedTrack, playlist)
            _state.value = BottomSheetPlaylistState.TrackAdded(result, playlist.name)
        }
    }

    init {
        loadPlaylistItems()
    }

    private fun loadPlaylistItems() {
        viewModelScope.launch {
            val playlists = playlistsInteractor.getAllPlaylistWithTracks()
            _state.value = BottomSheetPlaylistState.Loading(playlists)
        }
    }
}