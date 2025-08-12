package com.practicum.playlistmaker.medialibrary.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.common.data.domain.entity.Playlist
import com.practicum.playlistmaker.common.data.domain.entity.Track
import com.practicum.playlistmaker.medialibrary.domain.PlaylistsInteractor
import kotlinx.coroutines.launch

class PlaylistScreenViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    private val state = MutableLiveData<PlaylistScreenState>()

    lateinit var loadedPlaylist: Playlist

    fun getState(): LiveData<PlaylistScreenState> {
        return state
    }

    fun countMinutes(playlist: Playlist): Long {
        var millis: Long = 0
        playlist.trackList?.forEach { track ->
            millis += track.trackTime

        }
        return millis / 60000
    }

    fun loadPlaylist(playlist: Playlist) {
        loadedPlaylist = playlist
        state.value = PlaylistScreenState.Loading(playlist, countMinutes(playlist))
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

    fun deleteTrack(track: Track) {
        viewModelScope.launch {
            playlistsInteractor.removeTrackFromPlaylist(track, loadedPlaylist)
            loadedPlaylist =  playlistsInteractor.getPlaylistWithTracks(loadedPlaylist.id)
            state.value = PlaylistScreenState.Loading(loadedPlaylist, countMinutes(loadedPlaylist))

        }
    }
}