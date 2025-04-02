package com.practicum.playlistmaker.common.data.domain.api

interface Consumer<T> {
    fun consume(data: ConsumerData<T>)
}
