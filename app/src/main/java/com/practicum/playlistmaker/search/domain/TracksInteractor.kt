package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.common.data.domain.api.Consumer
import com.practicum.playlistmaker.common.data.domain.entity.TrackResponse


interface TracksInteractor {

    fun getTracks(expression: String, consumer: Consumer<TrackResponse>)

}