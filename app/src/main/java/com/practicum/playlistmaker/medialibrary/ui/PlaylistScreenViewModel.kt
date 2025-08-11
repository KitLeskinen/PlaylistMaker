package com.practicum.playlistmaker.medialibrary.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.common.data.domain.entity.Playlist
import com.practicum.playlistmaker.medialibrary.domain.PlaylistsInteractor
import kotlinx.coroutines.launch

class PlaylistScreenViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    private val state = MutableLiveData<PlaylistScreenState>()

    fun getState(): LiveData<PlaylistScreenState> {
        return state
    }

    fun loadPlaylist(playlist: Playlist){
        state.value = PlaylistScreenState.Loading(playlist)
    }

//    fun updatePlaylistItems() {
//        loadPlaylistItems()
//    }

//    init {
//        loadPlaylistItems()
//    }

    private fun loadPlaylistItems() {
        viewModelScope.launch {
//            val playlists = playlistsInteractor.getAllPlaylistWithTracks()
//            state.value = PlaylistState.Loading(playlists)
        }
    }
}