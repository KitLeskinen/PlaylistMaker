package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.common.data.domain.entity.TrackResponse


interface TracksRepository {

    fun searchTracks(expression: String): TrackResponse

}