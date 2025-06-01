package com.practicum.playlistmaker.medialibrary.ui

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.common.data.domain.entity.Track
import com.practicum.playlistmaker.medialibrary.domain.FavoritesInteractor

class FavoritesViewModel(private val favoritesInteractor: FavoritesInteractor) : ViewModel() {



    init {
        val track  = Track(
            trackId = 12,
            trackName = "test",
            artistName = "test",
            trackTime = 12,
            artworkUrl100 = "test",
            country = "test",
            primaryGenreName = "test",
            releaseDate = "test",
            collectionName = "test",
            previewUrl = "test",
            isFavorite = false
        )
        val tracks: List<Track> = mutableListOf(track)


    }

}