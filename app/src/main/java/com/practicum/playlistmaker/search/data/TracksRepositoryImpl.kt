package com.practicum.playlistmaker.search.data

import android.util.Log
import com.practicum.playlistmaker.common.data.NetworkClient
import com.practicum.playlistmaker.search.data.model.TracksSearchResponse
import com.practicum.playlistmaker.common.data.domain.entity.TrackResponse
import com.practicum.playlistmaker.data.model.TracksSearchRequest
import com.practicum.playlistmaker.search.domain.TracksRepository


class TracksRepositoryImpl(private val networkClient: NetworkClient) :
    TracksRepository {
    override fun searchTracks(expression: String): TrackResponse {

        val response = networkClient.doRequest(TracksSearchRequest(expression))


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
            return TrackResponse(mappedTracks, false, "")

        } else {
            Log.d("NETWORK RESPONSE", "searchTracks: ${response.resultCode}")
            return TrackResponse(emptyList(), true, response.resultCode.toString())
        }
    }
}





