package com.practicum.playlistmaker.audio_player.ui

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.audio_player.domain.AudioPlayerInteractor
import com.practicum.playlistmaker.common.data.domain.OnPreparedAudioPlayerListener
import com.practicum.playlistmaker.common.data.domain.api.OnCompletionListener
import com.practicum.playlistmaker.common.data.domain.entity.Track

class AudioPlayerViewModel(
    private val track: Track,
    private val audioPlayerInteractor: AudioPlayerInteractor
) : ViewModel() {

    private var handler: Handler? = Handler(Looper.getMainLooper())

    private var runnable: Runnable? = null



    private val state = MutableLiveData<AudioPlayerState>()

    init {
        loadData()
    }

    private fun loadData() {
        state.value = AudioPlayerState.Loading(track)
        audioPlayerInteractor.prepare(
            onPreparedAudioPlayerListener = object : OnPreparedAudioPlayerListener {
                override fun invoke() {
                    state.value = AudioPlayerState.Preparing

                }
            },
            onCompletionListener = object : OnCompletionListener {
                override fun invoke() {
                    runnable?.let { handler?.removeCallbacks(it) }
                    state.value = AudioPlayerState.Stopped
                }
            }
        )
    }

    fun getState(): LiveData<AudioPlayerState> = state

    companion object {
        private const val DELAY = 500L
    }

    fun togglePlayer() {

        when (state.value) {
            is AudioPlayerState.StartPlaying -> {
                audioPlayerInteractor.pause()
                runnable?.let { handler?.removeCallbacks(it) }
                state.value = AudioPlayerState.Paused
            }

            is AudioPlayerState.Playback -> {
                audioPlayerInteractor.pause()
                state.value = AudioPlayerState.Paused
                runnable?.let { handler?.removeCallbacks(it) }

            }

            is AudioPlayerState.Loading, AudioPlayerState.Preparing, AudioPlayerState.Paused -> {
                Log.d("PLAYER", "togglePlayer:  state.value = AudioPlayerState.Playing ")
                audioPlayerInteractor.play()
                state.value = AudioPlayerState.StartPlaying

                runnable = createCurrentPlayTimer()
                handler?.post(createCurrentPlayTimer())
            }

            is AudioPlayerState.Stopped -> {
                audioPlayerInteractor.play()
                state.value = AudioPlayerState.Playback(audioPlayerInteractor.getCurrentPosition())
            }

            else -> {

            }
        }
    }

    private fun createCurrentPlayTimer(): Runnable {
        return object : Runnable {
            override fun run() {
                Log.d("RUNNABLE", "run: ${audioPlayerInteractor.getCurrentPosition()}")
                state.value = AudioPlayerState.Playback(audioPlayerInteractor.getCurrentPosition())
                runnable = this
                handler?.postDelayed(this, DELAY)

            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerInteractor.pause()
        runnable?.let { handler?.removeCallbacks(it) }
        Log.d("DEBUG", "onCleared: ")
    }

}