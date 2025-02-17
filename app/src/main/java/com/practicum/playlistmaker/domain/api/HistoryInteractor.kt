package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.entity.Track

interface HistoryInteractor {

    fun getTracksHistory() : MutableList<Track>

    fun saveTracksHistory(mutableList: MutableList<Track>)

    fun clearHistory()
}