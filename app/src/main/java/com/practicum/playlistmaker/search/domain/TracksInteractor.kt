package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.common.data.domain.entity.TrackResponse
import kotlinx.coroutines.flow.Flow


interface TracksInteractor {

    fun getTracks(expression: String): Flow<TrackResponse>

}