package com.practicum.playlistmaker.audio_player.impl

import com.practicum.playlistmaker.audio_player.domain.AudioPlayerInteractor
import com.practicum.playlistmaker.audio_player.domain.AudioPlayerRepository
import com.practicum.playlistmaker.common.data.domain.OnPreparedAudioPlayerListener
import com.practicum.playlistmaker.common.data.domain.api.OnCompletionListener


class AudioPlayerIneractorImpl(private val repository: AudioPlayerRepository) :
    AudioPlayerInteractor {

    override fun prepare(previewUrl: String, onPreparedAudioPlayerListener: OnPreparedAudioPlayerListener, onCompletionListener: OnCompletionListener) {
        repository.prepare(previewUrl, onPreparedAudioPlayerListener, onCompletionListener)
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

    override fun reset() {
        repository.reset()
    }

}