package com.practicum.playlistmaker.domain.api


import com.practicum.playlistmaker.domain.entity.TrackResponse

interface TracksRepository {

    fun searchTracks(expression: String): TrackResponse

}