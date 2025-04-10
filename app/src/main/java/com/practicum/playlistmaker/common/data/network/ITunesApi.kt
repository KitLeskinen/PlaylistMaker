package com.practicum.playlistmaker.common.data.network

import com.practicum.playlistmaker.search.data.model.TracksSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    fun search(
        @Query("term") text: String
    ): Call<TracksSearchResponse>
}