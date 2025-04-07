package com.practicum.playlistmaker.audio_player.ui

import com.practicum.playlistmaker.common.data.domain.entity.Track

sealed interface AudioPlayerState {
    data class Loading(val track: Track): AudioPlayerState
    object Preparing: AudioPlayerState
    object StartPlaying: AudioPlayerState
    class Playback(val timePositionState: Int): AudioPlayerState
    object Paused: AudioPlayerState
    object Stopped: AudioPlayerState
    data class Content(val data: Track) : AudioPlayerState
}