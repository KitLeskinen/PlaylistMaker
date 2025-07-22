package com.practicum.playlistmaker.audio_player.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.common.data.domain.entity.Playlist
import com.practicum.playlistmaker.common.data.domain.entity.Track
import com.practicum.playlistmaker.medialibrary.domain.PlaylistsInteractor
import kotlinx.coroutines.launch

class BottomSheetPlaylistViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    private val state = MutableLiveData<BottomSheetPlaylistState>()

    fun getState(): LiveData<BottomSheetPlaylistState> {
        return state
    }

    fun updatePlaylistItems() {
        loadPlaylistItems()
    }

   fun addTrackToPlayList(selectedTrack: Track, playlist: Playlist){
    viewModelScope.launch {
        Log.d("TAG", "addTrackToPlayList: ${selectedTrack.trackName} ${playlist.name}")
        playlistsInteractor.addTrack(selectedTrack, playlist)
    }}

    init {
        loadPlaylistItems()
    }

    private fun loadPlaylistItems() {
        viewModelScope.launch {
            val playlists = playlistsInteractor.getAllPlaylistWithTracks()
            state.value = BottomSheetPlaylistState.Loading(playlists)
        }
    }
}