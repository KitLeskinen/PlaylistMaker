package com.practicum.playlistmaker.data.network


import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.model.NetResponse
import com.practicum.playlistmaker.data.model.TracksSearchRequest

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


class RetrofitNetworkClient : NetworkClient {

    override fun doRequest(dto: Any): NetResponse {

        try {
            val retrofit =
                Retrofit.Builder().baseUrl("https://itunes.apple.com").addConverterFactory(
                    GsonConverterFactory.create()
                ).build()
            val iTunesApi = retrofit.create<ITunesApi>()

            if (dto is TracksSearchRequest) {
                val resp = iTunesApi.search(dto.expression).execute()

               val netResponse = resp.body() ?: NetResponse()

                netResponse.apply { resultCode = resp.code() }

                return netResponse

            } else {
                return NetResponse().apply { resultCode = 400 }
            }
        } catch (ex: Exception){
            return NetResponse().apply { resultCode = 500 }
        }


    }
}