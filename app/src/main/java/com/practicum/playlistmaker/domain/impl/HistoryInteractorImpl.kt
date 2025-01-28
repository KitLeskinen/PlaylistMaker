package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.HistoryInteractor
import com.practicum.playlistmaker.domain.api.HistoryRepository
import com.practicum.playlistmaker.domain.entity.Track

class HistoryInteractorImpl(private val repository: HistoryRepository) :  HistoryInteractor {
    override fun getTracksHistory(): MutableList<Track> {
        return repository.getTracksHistory()
    }

    override fun saveTracksHistory(mutableList: MutableList<Track>) {
        repository.saveTracksHistory(mutableList)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}