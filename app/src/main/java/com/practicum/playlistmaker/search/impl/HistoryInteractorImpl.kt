package com.practicum.playlistmaker.search.impl

import com.practicum.playlistmaker.search.domain.HistoryInteractor
import com.practicum.playlistmaker.search.domain.HistoryRepository


class HistoryInteractorImpl(private val repository: HistoryRepository) :
    HistoryInteractor {

    override fun getTracksHistory(): MutableList<com.practicum.playlistmaker.common.data.domain.entity.Track> {
        return repository.getTracksHistory()
    }

    override fun saveTracksHistory(mutableList: MutableList<com.practicum.playlistmaker.common.data.domain.entity.Track>) {
        repository.saveTracksHistory(mutableList)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}