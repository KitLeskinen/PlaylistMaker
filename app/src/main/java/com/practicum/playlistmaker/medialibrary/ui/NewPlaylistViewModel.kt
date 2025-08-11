package com.practicum.playlistmaker.medialibrary.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.common.data.domain.entity.Playlist
import com.practicum.playlistmaker.medialibrary.domain.PlaylistsInteractor
import kotlinx.coroutines.launch

class NewPlaylistViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    private val state = MutableLiveData<NewPlayListState>()

    fun getState(): LiveData<NewPlayListState>{
        return state
    }

    init {
        state.value = NewPlayListState.Loading
    }

    fun savePlaylist(playlist: Playlist){
        viewModelScope.launch {
            playlistsInteractor.savePlaylist(playlist)
        }
    }

    fun textChanged(text: CharSequence?) {
        if(text.isNullOrEmpty()){
            state.value = NewPlayListState.EmptyFields
        } else {
            state.value = NewPlayListState.FilledFields
        }

    }

    fun saveCoverImage(uriString: String){
        val uri = playlistsInteractor.saveCoverImage(uriString)
        state.value = NewPlayListState.CoverFilled(uri)
    }


}