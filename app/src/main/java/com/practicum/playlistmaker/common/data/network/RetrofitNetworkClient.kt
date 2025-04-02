package com.practicum.playlistmaker.common.data.network


import com.practicum.playlistmaker.Creator.Creator
import com.practicum.playlistmaker.common.data.NetworkClient
import com.practicum.playlistmaker.data.model.TracksSearchRequest
import com.practicum.playlistmaker.search.data.model.NetResponse



class RetrofitNetworkClient : NetworkClient {

    override fun doRequest(dto: Any): NetResponse {

        try {

            val iTunesApi = Creator.provideITunesApi()
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