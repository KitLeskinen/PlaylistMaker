package com.practicum.playlistmaker.audio_player.domain

import com.practicum.playlistmaker.common.data.domain.OnPreparedAudioPlayerListener
import com.practicum.playlistmaker.common.data.domain.api.OnCompletionListener

interface AudioPlayerInteractor {

    fun prepare(onPreparedAudioPlayerListener: OnPreparedAudioPlayerListener, onCompletionListener: OnCompletionListener)

    fun play()

    fun pause()

    fun release()

    fun getCurrentPosition() : Int

    fun reset()

}