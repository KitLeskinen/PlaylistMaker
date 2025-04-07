package com.practicum.playlistmaker.common.data.domain.impl


import com.practicum.playlistmaker.common.data.domain.api.Consumer
import com.practicum.playlistmaker.common.data.domain.api.ConsumerData
import com.practicum.playlistmaker.search.domain.TracksInteractor

import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.common.data.domain.entity.TrackResponse

import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) :
    TracksInteractor {


    private val executor = Executors.newCachedThreadPool()


    override fun getTracks(expression: String, consumer: Consumer<TrackResponse>) {

        executor.execute {
            val trackResponse = repository.searchTracks(expression)
            if (!trackResponse.isError) {
                consumer.consume(ConsumerData.Data(trackResponse))
            } else {
                consumer.consume(ConsumerData.Error(trackResponse.errorMessage))
            }
        }

    }

}