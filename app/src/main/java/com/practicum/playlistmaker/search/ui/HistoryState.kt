package com.practicum.playlistmaker.search.ui

import com.practicum.playlistmaker.common.data.domain.entity.Track

sealed interface HistoryState {
    data class HistoryTrackAdded(val list: MutableList<Track>): HistoryState
    object HistoryCleared: HistoryState
}