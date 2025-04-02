package com.practicum.playlistmaker.common.data

import com.practicum.playlistmaker.search.data.model.NetResponse

interface NetworkClient {
    fun doRequest(dto: Any) : NetResponse
}