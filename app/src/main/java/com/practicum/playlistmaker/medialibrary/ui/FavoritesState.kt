package com.practicum.playlistmaker.medialibrary.ui

import com.practicum.playlistmaker.common.data.domain.entity.Track

sealed interface FavoritesState {

    data object Loading : FavoritesState

    data class Content(
        val tracks: List<Track>
    ) : FavoritesState

    data class Empty(
        val message: String
    ) : FavoritesState

}