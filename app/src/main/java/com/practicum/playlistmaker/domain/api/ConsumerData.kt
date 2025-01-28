package com.practicum.playlistmaker.domain.api

sealed interface ConsumerData<T> {
    data class Data<T>(val value: T) : ConsumerData<T>
    data class Error<T>(val message: String) : ConsumerData<T>
}