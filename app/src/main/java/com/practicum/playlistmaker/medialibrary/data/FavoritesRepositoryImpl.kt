package com.practicum.playlistmaker.medialibrary.data

import com.practicum.playlistmaker.common.data.domain.entity.Track
import com.practicum.playlistmaker.medialibrary.data.converters.TrackDbConvertor
import com.practicum.playlistmaker.medialibrary.data.db.AppDataBase
import com.practicum.playlistmaker.medialibrary.data.db.entity.TrackEntity
import com.practicum.playlistmaker.medialibrary.domain.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class FavoritesRepositoryImpl(private val appDataBase: AppDataBase, private val trackDbConvertor: TrackDbConvertor) : FavoritesRepository {

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDataBase.trackDao().getTracks()
        emit((convertFromTrackEntity(tracks)))

    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track>{
        return tracks.map { track -> trackDbConvertor.map(track) }
    }

    override suspend fun addTrackToFavorites(tracksList: List<Track>) {
        val tracksEntity = tracksList.map { original ->
            trackDbConvertor.map(original)
        }
        appDataBase.trackDao().insertTrack(tracksEntity)

    }

    override suspend fun deleteFavoriteTrack(track: Track) {
        appDataBase.trackDao().delete(trackDbConvertor.map(track))
    }

    override fun getFavoriteIds(): Flow<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun isFavoriteTrack(track: Track): Boolean {
        val tracks: List<TrackEntity> = appDataBase.trackDao().getTrack(track.trackId)
        return tracks.isNotEmpty()
    }
}