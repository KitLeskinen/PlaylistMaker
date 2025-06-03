package com.practicum.playlistmaker.medialibrary.impl

import com.practicum.playlistmaker.common.data.domain.entity.Track
import com.practicum.playlistmaker.medialibrary.domain.FavoritesInteractor
import com.practicum.playlistmaker.medialibrary.domain.FavoritesRepository
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(private val repository: FavoritesRepository) : FavoritesInteractor {

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return repository.getFavoriteTracks()
    }

    override suspend fun addTrackToFavorites(tracks: List<Track>) {
        repository.addTrackToFavorites(tracks)
    }

    override suspend fun deleteFavoriteTrack(track: Track) {
        repository.deleteFavoriteTrack(track)
    }

    override fun getFavoriteIds(): Flow<Int> {
        return repository.getFavoriteIds()
    }

    override suspend fun isFavoriteTrack(track: Track): Boolean {
        return repository.isFavoriteTrack(track)
    }

}