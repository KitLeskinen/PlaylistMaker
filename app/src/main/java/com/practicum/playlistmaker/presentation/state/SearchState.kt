package com.practicum.playlistmaker.presentation.state

sealed interface SearchState {
    object Responsing: SearchState

    data class Error(val message: String) : SearchState
    object ResultEmpty: SearchState
    object Result: SearchState
}