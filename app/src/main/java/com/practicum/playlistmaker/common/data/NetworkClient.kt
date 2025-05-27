package com.practicum.playlistmaker.common.data

import com.practicum.playlistmaker.search.data.model.NetResponse

interface NetworkClient {
    suspend fun doRequestSuspend(dto: Any) : NetResponse
}