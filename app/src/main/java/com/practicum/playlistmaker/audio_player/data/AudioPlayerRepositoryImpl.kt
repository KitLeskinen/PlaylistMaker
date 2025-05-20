package com.practicum.playlistmaker.audio_player.data

import android.media.MediaPlayer
import android.util.Log
import com.practicum.playlistmaker.audio_player.domain.AudioPlayerRepository
import com.practicum.playlistmaker.common.data.domain.OnPreparedAudioPlayerListener
import com.practicum.playlistmaker.common.data.domain.api.OnCompletionListener
import com.practicum.playlistmaker.common.data.domain.entity.Track


class AudioPlayerRepositoryImpl(private val track: Track) : AudioPlayerRepository {


    var mediaPlayer =  MediaPlayer()


    override fun prepare(previewUrl: String, onPreparedAudioPlayerListener: OnPreparedAudioPlayerListener, onCompletionListener: OnCompletionListener){
        Log.d("AUDIO", "Track previewUrl: '${track.previewUrl}'")
        mediaPlayer.reset()
        mediaPlayer.setDataSource(previewUrl)

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

    override fun reset() {
        mediaPlayer.reset()
    }

}