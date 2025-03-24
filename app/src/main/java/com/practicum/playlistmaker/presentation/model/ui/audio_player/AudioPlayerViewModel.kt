package com.practicum.playlistmaker.presentation.model.ui.audio_player

import android.icu.text.SimpleDateFormat
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.api.AudioPlayerInteractor
import com.practicum.playlistmaker.domain.api.OnCompletionListener
import com.practicum.playlistmaker.domain.api.OnPreparedAudioPlayerListener
import com.practicum.playlistmaker.domain.entity.Track
import com.practicum.playlistmaker.presentation.state.AudioPlayerState
import java.util.Locale

class AudioPlayerViewModel(
    private val track: Track,
    private val audioPlayerInteractor: AudioPlayerInteractor
) : ViewModel() {

    private var handler: Handler? = Handler(Looper.getMainLooper())

    private lateinit var runnable: Runnable

    fun getTrack(): Track {
        return track
    }

    //    private val audioPlayerInteractor = Creator.provideAudioPlayerInteractor()
//
    private val state = MutableLiveData<AudioPlayerState>(AudioPlayerState.Loading)

    private val timePosition = MutableLiveData<Int>()

    fun getTimePositionState(): MutableLiveData<Int> {
        return timePosition
    }


    init {
        loadData()
    }

    private fun loadData() {
        state.value = AudioPlayerState.Loading
        audioPlayerInteractor.prepare(
            onPreparedAudioPlayerListener = object : OnPreparedAudioPlayerListener {
                override fun invoke() {
                    state.value = AudioPlayerState.Preparing

                }
            },
            onCompletionListener = object : OnCompletionListener {
                override fun invoke() {
                    state.value = AudioPlayerState.Stopped
                }
            }
        )
    }

    fun getState(): LiveData<AudioPlayerState> = state

    companion object {
        fun factory(
            track: Track,
            audioPlayerInteractor: AudioPlayerInteractor
        ): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    AudioPlayerViewModel(track, audioPlayerInteractor)
                }
            }
        }

        private const val DELAY = 500L
    }

    fun togglePlayer() {

        when (state.value) {
            is AudioPlayerState.Playing -> {
                audioPlayerInteractor.pause()
                state.value = AudioPlayerState.Paused
                handler?.removeCallbacks(runnable)
            }

            is AudioPlayerState.Loading, AudioPlayerState.Preparing, AudioPlayerState.Paused -> {
                Log.d("PLAYER", "togglePlayer:  state.value = AudioPlayerState.Playing ")
                audioPlayerInteractor.play()
                state.value = AudioPlayerState.Playing
                runnable = createCurrentPlayTimer()
                handler?.post(createCurrentPlayTimer())
            }
            is AudioPlayerState.Stopped -> {
                audioPlayerInteractor.play()
                state.value = AudioPlayerState.Playing
            }
            else -> {

            }
        }
    }

    private fun createCurrentPlayTimer(): Runnable {
        return object : Runnable {
            override fun run() {
                if (state.value == AudioPlayerState.Playing) {
                    Log.d("RUNNABLE", "run: ${audioPlayerInteractor.getCurrentPosition()}")
                    timePosition.postValue(audioPlayerInteractor.getCurrentPosition())
                    handler?.postDelayed(this, DELAY)
                }
                //   }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        handler?.removeCallbacks(runnable)
    }

}