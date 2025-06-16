package com.practicum.playlistmaker.medialibrary.ui

import android.net.Uri

sealed interface NewPlayListState {
    data object Loading: NewPlayListState
    data object EmptyFields: NewPlayListState
    data object FilledFields: NewPlayListState
    data class CoverFilled(val uri: Uri): NewPlayListState
}