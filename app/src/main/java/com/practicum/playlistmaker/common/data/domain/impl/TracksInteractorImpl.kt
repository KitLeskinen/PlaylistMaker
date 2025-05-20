package com.practicum.playlistmaker.common.data.domain.impl


import com.practicum.playlistmaker.common.data.domain.entity.TrackResponse
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.TracksRepository
import kotlinx.coroutines.flow.Flow


class TracksInteractorImpl(private val repository: TracksRepository) :
    TracksInteractor {


    override fun getTracks(expression: String): Flow<TrackResponse> {
        return repository.searchTracks(expression)


    }


}