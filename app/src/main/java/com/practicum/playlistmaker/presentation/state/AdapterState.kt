package com.practicum.playlistmaker.presentation.state

import com.practicum.playlistmaker.domain.entity.Track

sealed interface AdapterState {
    data class Loading(val history: List<Track>): AdapterState
    data class AdapterUpdated(val history: List<Track>): AdapterState
    object AdapterSearch: AdapterState
}