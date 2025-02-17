package com.practicum.playlistmaker.domain.api

interface Consumer<T> {
    fun consume(data: ConsumerData<T>)
}
