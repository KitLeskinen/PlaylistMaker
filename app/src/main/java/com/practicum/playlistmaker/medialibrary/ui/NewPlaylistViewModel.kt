package com.practicum.playlistmaker.medialibrary.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.common.data.domain.entity.Playlist
import com.practicum.playlistmaker.medialibrary.domain.PlaylistsInteractor
import kotlinx.coroutines.launch

open class NewPlaylistViewModel(protected val playlistsInteractor: PlaylistsInteractor) :
    ViewModel() {


    protected val state = MutableLiveData<NewPlayListState>()

    fun getState(): LiveData<NewPlayListState> {
        return state
    }

    protected open val playlistId: Long? = null

    init {
        state.value = NewPlayListState.Loading
    }

    open fun savePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistsInteractor.savePlaylist(playlist)
        }
    }

    fun textChanged(text: CharSequence?) {
        if (text.isNullOrEmpty()) {
            state.value = NewPlayListState.EmptyFields
        } else {
            state.value = NewPlayListState.FilledFields
        }

    }

    fun saveCoverImage(uriString: String) {
        val uri = playlistsInteractor.saveCoverImage(uriString)
        state.value = NewPlayListState.CoverFilled(uri)
    }


}