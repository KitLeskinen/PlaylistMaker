package com.practicum.playlistmaker.medialibrary.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.FavoritesInteractor
import kotlinx.coroutines.launch

class FavoritesViewModel(private val favoritesInteractor: FavoritesInteractor) : ViewModel() {

    private val state = MutableLiveData<FavoritesListState>()

    fun getState(): LiveData<FavoritesListState> {
        return state
    }


     fun loadFavoriteTracks() {
        viewModelScope.launch {
            state.value = FavoritesListState.Loading
            try {
                favoritesInteractor.getFavoriteTracks()
                    .collect { tracks ->
                        if (tracks.isEmpty()) {
                            state.value = FavoritesListState.Empty
                        } else {
                            state.value = FavoritesListState.Content(tracks)
                        }

                    }

            } catch (e: Exception) {
                state.value = FavoritesListState.Error("Ошибка")
            }


        }
    }

}