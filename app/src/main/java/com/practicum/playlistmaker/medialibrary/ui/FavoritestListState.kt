package com.practicum.playlistmaker.medialibrary.ui

import com.practicum.playlistmaker.common.data.domain.entity.Track


sealed class FavoritesListState {
    data object Loading: FavoritesListState()
    data class Content(val favoritesTrackList: List<Track>) : FavoritesListState()
    data object Empty: FavoritesListState()
    data class Error(val message: String) : FavoritesListState()
}