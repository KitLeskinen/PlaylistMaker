package com.practicum.playlistmaker.search.ui

import com.practicum.playlistmaker.common.data.domain.entity.Track

sealed interface AdapterState {
    data class Loading(val history: List<Track>): AdapterState
    data class AdapterUpdated(val history: List<Track>): AdapterState
    object AdapterSearch: AdapterState
}