package com.practicum.playlistmaker.audio_player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.audio_player.domain.AudioPlayerInteractor
import com.practicum.playlistmaker.common.data.domain.OnPreparedAudioPlayerListener
import com.practicum.playlistmaker.common.data.domain.api.OnCompletionListener
import com.practicum.playlistmaker.common.data.domain.entity.Track
import com.practicum.playlistmaker.medialibrary.domain.FavoritesInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val track: Track,
    private val audioPlayerInteractor: AudioPlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {


    private var timerJob: Job? = null

    private val state = MutableLiveData<AudioPlayerState>()

    private val favoritesState = MutableLiveData<FavoritesState>()

    init {
        loadData()
    }


    private fun loadData() {
        state.value = AudioPlayerState.Loading(track)

        audioPlayerInteractor.prepare(track.previewUrl,
            onPreparedAudioPlayerListener = object : OnPreparedAudioPlayerListener {
                override fun invoke() {
                    state.value = AudioPlayerState.Preparing
                }
            },
            onCompletionListener = object : OnCompletionListener {
                override fun invoke() {
                    timerJob?.cancel()
                    state.value = AudioPlayerState.Stopped
                }
            }
        )
        viewModelScope.launch {
            favoritesState.value = FavoritesState.FavoritesChanged(favoritesInteractor.isFavoriteTrack(track))
        }
    }

    fun getState(): LiveData<AudioPlayerState> = state

    fun getFavoritesState(): LiveData<FavoritesState> = favoritesState

    companion object {
        private const val DELAY = 500L
    }

    fun togglePlayer() {

        when (state.value) {
            is AudioPlayerState.StartPlaying -> {
                audioPlayerInteractor.pause()
                timerJob?.cancel()
                state.value = AudioPlayerState.Paused
            }

            is AudioPlayerState.Playback -> {
                audioPlayerInteractor.pause()
                state.value = AudioPlayerState.Paused
                timerJob?.cancel()


            }

            is AudioPlayerState.Loading, AudioPlayerState.Preparing, AudioPlayerState.Paused -> {
                audioPlayerInteractor.play()
                state.value = AudioPlayerState.StartPlaying
                timerJob?.cancel()
                timerJob = viewModelScope.launch {
                    updateCurrentTimePosition()
                }


            }

            is AudioPlayerState.Stopped -> {
                audioPlayerInteractor.play()
                state.value = AudioPlayerState.Playback(audioPlayerInteractor.getCurrentPosition())
            }

            else -> {

            }
        }
    }

    private suspend fun updateCurrentTimePosition() {
        while (state.value is AudioPlayerState.Playback || state.value is AudioPlayerState.StartPlaying) {
            delay(DELAY)
            state.value = AudioPlayerState.Playback(audioPlayerInteractor.getCurrentPosition())
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerInteractor.pause()
        timerJob?.cancel()
        audioPlayerInteractor.reset()
    }

    fun switchFavorites() {
        viewModelScope.launch {

            if (!favoritesInteractor.isFavoriteTrack(track)) {
                favoritesInteractor.addTrackToFavorites(listOf(track))
                favoritesState.value = FavoritesState.FavoritesChanged(true)
            } else {
                favoritesInteractor.deleteFavoriteTrack(track)
                favoritesState.value = FavoritesState.FavoritesChanged(false)
            }

        }
    }

}