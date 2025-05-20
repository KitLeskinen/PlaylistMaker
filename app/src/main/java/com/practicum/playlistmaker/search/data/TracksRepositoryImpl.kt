package com.practicum.playlistmaker.search.data

import android.util.Log
import com.practicum.playlistmaker.common.data.NetworkClient
import com.practicum.playlistmaker.common.data.domain.entity.TrackResponse
import com.practicum.playlistmaker.data.model.TracksSearchRequest
import com.practicum.playlistmaker.search.data.model.TracksSearchResponse
import com.practicum.playlistmaker.search.domain.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class TracksRepositoryImpl(private val networkClient: NetworkClient) :
    TracksRepository {
    override fun searchTracks(expression: String): Flow<TrackResponse> = flow{

        val response = networkClient.doRequestSuspend(TracksSearchRequest(expression))


        if (response.resultCode == 200) {
            val mappedTracks = (response as TracksSearchResponse).results.map {

                com.practicum.playlistmaker.common.data.domain.entity.Track(
                    trackId = it.trackId,
                    trackName = it.trackName,
                    artistName = it.artistName,
                    trackTime = it.trackTime,
                    artworkUrl100 = it.artworkUrl100,
                    country = it.country,
                    primaryGenreName = it.primaryGenreName,
                    releaseDate = it.releaseDate,
                    collectionName = it.collectionName,
                    previewUrl = it.previewUrl,
                )
            }
            Log.d("NETWORK RESPONSE", "searchTracks: ${response.resultCode}")
            emit(TrackResponse(mappedTracks, false, ""))

        } else {
            Log.d("NETWORK RESPONSE", "searchTracks: ${response.resultCode}")
            emit(TrackResponse(emptyList(), true, response.resultCode.toString()))
        }
    }
}





