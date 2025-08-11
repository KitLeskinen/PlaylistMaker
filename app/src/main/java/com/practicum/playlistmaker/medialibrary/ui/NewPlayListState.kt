package com.practicum.playlistmaker.medialibrary.ui

sealed interface NewPlayListState {
    data object Loading: NewPlayListState
    data object EmptyFields: NewPlayListState
    data object FilledFields: NewPlayListState
    data class CoverFilled(val uriString: String): NewPlayListState
}