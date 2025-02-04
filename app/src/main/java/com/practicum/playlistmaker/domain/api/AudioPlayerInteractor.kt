package com.practicum.playlistmaker.domain.api

interface AudioPlayerInteractor {

    fun prepare(onPreparedAudioPlayerListener: OnPreparedAudioPlayerListener, onCompletionListener: OnCompletionListener)

    fun play()

    fun pause()

    fun release()

    fun getCurrentPosition() : Int

}