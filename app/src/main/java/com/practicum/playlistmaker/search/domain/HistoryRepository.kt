package com.practicum.playlistmaker.search.domain

interface HistoryRepository {

    fun getTracksHistory(): MutableList<com.practicum.playlistmaker.common.data.domain.entity.Track>

    fun saveTracksHistory(mutableList: MutableList<com.practicum.playlistmaker.common.data.domain.entity.Track>)

    fun clearHistory()

}