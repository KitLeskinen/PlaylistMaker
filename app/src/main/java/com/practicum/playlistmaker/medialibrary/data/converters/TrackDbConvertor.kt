package com.practicum.playlistmaker.medialibrary.data.converters

import com.practicum.playlistmaker.common.data.domain.entity.Track
import com.practicum.playlistmaker.medialibrary.data.db.entity.TrackEntity
import com.practicum.playlistmaker.search.data.model.TrackDto


class TrackDbConvertor {
    fun map(track: TrackDto): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.country,
            track.primaryGenreName,
            track.releaseDate,
            track.collectionName,
            track.previewUrl,
            track.isFavorite
        )
    }

    fun map(track: TrackEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.country,
            track.primaryGenreName,
            track.releaseDate,
            track.collectionName,
            track.previewUrl,
            false
        )
    }

    fun map(track: Track): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.country,
            track.primaryGenreName,
            track.releaseDate,
            track.collectionName,
            track.previewUrl,
            false
        )
    }

}