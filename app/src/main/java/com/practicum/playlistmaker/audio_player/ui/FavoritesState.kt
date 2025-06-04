package com.practicum.playlistmaker.audio_player.ui

sealed interface FavoritesState {
    data class FavoritesChanged(val isFavorite: Boolean) : FavoritesState

}