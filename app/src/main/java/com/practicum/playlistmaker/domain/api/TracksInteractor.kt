package com.practicum.playlistmaker.domain.api


import com.practicum.playlistmaker.domain.entity.TrackResponse

interface TracksInteractor {

    fun getTracks(expression: String, consumer: Consumer<TrackResponse>)

}