package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.AudioPlayerInteractor
import com.practicum.playlistmaker.domain.api.AudioPlayerRepository
import com.practicum.playlistmaker.domain.api.OnCompletionListener
import com.practicum.playlistmaker.domain.api.OnPreparedAudioPlayerListener

class AudioPlayerIneractorImpl(private val repository: AudioPlayerRepository) : AudioPlayerInteractor {

    override fun prepare(onPreparedAudioPlayerListener: OnPreparedAudioPlayerListener, onCompletionListener: OnCompletionListener) {
        repository.prepare(onPreparedAudioPlayerListener, onCompletionListener)
    }

    override fun play() {
        repository.play()
    }

    override fun pause() {
        repository.pause()
    }

    override fun release(){
        repository.release()
    }

    override fun getCurrentPosition(): Int {
        return repository.getCurrentPosition()
    }

}