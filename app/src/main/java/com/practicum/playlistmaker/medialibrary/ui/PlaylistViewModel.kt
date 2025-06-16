package com.practicum.playlistmaker.medialibrary.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.PlaylistsInteractor
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    private val state = MutableLiveData<PlaylistState>()

    fun getState(): LiveData<PlaylistState> {
        return state
    }

    fun updatePlaylistItems() {
        loadPlaylistItems()
    }

    init {
        loadPlaylistItems()
    }

    private fun loadPlaylistItems() {
        viewModelScope.launch {
            val playlists = playlistsInteractor.getAllPlaylistWithTracks()
            state.value = PlaylistState.Loading(playlists)
        }
    }
}