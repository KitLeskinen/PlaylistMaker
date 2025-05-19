package com.practicum.playlistmaker.common.data.network


import com.practicum.playlistmaker.common.data.NetworkClient
import com.practicum.playlistmaker.data.model.TracksSearchRequest
import com.practicum.playlistmaker.search.data.model.NetResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class RetrofitNetworkClient(private val iTunesApi: ITunesApi) : NetworkClient {



    override suspend fun doRequestSuspend(dto: Any): NetResponse {


        if (dto is TracksSearchRequest) {
            return withContext(Dispatchers.IO) {
                try {
                    val resp = iTunesApi.searchSuspend(dto.expression)
                    val netResponse = resp
                    netResponse.apply { resultCode = 200 }
                } catch (e: Throwable) {
                    NetResponse().apply { resultCode = 500 }
                }
            }
        } else {
            return NetResponse().apply { resultCode = 400 }
        }


    }
}