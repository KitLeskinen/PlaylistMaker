package com.practicum.playlistmaker.presentation.state

import com.practicum.playlistmaker.domain.entity.Track

sealed interface AudioPlayerState {
    object Loading: AudioPlayerState
    object Preparing: AudioPlayerState
    object Playing: AudioPlayerState
    object Paused: AudioPlayerState
    object Stopped: AudioPlayerState
    data class Content(val data: Track) : AudioPlayerState
}