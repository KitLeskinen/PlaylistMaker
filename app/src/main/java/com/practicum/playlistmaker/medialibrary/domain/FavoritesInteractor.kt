package com.practicum.playlistmaker.medialibrary.domain

import com.practicum.playlistmaker.common.data.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {

    fun getFavoriteTracks(): Flow<List<Track>>

    suspend fun addTrackToFavorites(tracks: List<Track>)

    suspend fun deleteFavoriteTrack(track: Track)

    fun getFavoriteIds(): Flow<Int>

    suspend fun isFavoriteTrack(track: Track): Boolean

}