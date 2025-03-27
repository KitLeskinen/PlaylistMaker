package com.practicum.playlistmaker.presentation.state

import com.practicum.playlistmaker.domain.entity.Track

sealed interface HistoryState {
    data class HistoryTrackAdded(val list: MutableList<Track>): HistoryState
    object HistoryCleared: HistoryState
}