package com.practicum.playlistmaker.audio_player.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.audio_player.domain.AudioPlayerRepository
import com.practicum.playlistmaker.common.data.domain.OnPreparedAudioPlayerListener
import com.practicum.playlistmaker.common.data.domain.api.OnCompletionListener
import com.practicum.playlistmaker.common.data.domain.entity.Track


class AudioPlayerRepositoryImpl(private val track: Track) : AudioPlayerRepository {


    var mediaPlayer =  MediaPlayer()


    override fun prepare(onPreparedAudioPlayerListener: OnPreparedAudioPlayerListener, onCompletionListener: OnCompletionListener){
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener {
            onPreparedAudioPlayerListener.invoke()

        }
        mediaPlayer.setOnCompletionListener {
            onCompletionListener.invoke()

        }

    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun play() {
        mediaPlayer.start()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

}