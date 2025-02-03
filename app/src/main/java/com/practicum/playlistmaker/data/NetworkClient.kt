package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.model.NetResponse

interface NetworkClient {
    fun doRequest(dto: Any) : NetResponse
}