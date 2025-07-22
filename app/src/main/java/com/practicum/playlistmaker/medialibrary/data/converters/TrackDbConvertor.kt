package com.practicum.playlistmaker.medialibrary.data.converters

import android.net.Uri
import com.practicum.playlistmaker.common.data.domain.entity.Playlist
import com.practicum.playlistmaker.common.data.domain.entity.Track
import com.practicum.playlistmaker.medialibrary.data.db.dao.PlaylistWithTracks
import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistEntity
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
            System.currentTimeMillis()
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
            track.previewUrl
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
            System.currentTimeMillis()
        )
    }

    fun map(playlistWithTracks: PlaylistWithTracks): Playlist{

        return Playlist(
            id = playlistWithTracks.playlist.id,
            name = playlistWithTracks.playlist.name,
            description = playlistWithTracks.playlist.description,
            coverUri = Uri.parse(playlistWithTracks.playlist.url),
            trackList = playlistWithTracks.tracks.map { trackEntity ->
                TrackDbConvertor().map(
                    trackEntity
                )
            }.toMutableList()

        )
    }

    fun map(playlist: Playlist) : PlaylistEntity{
        return PlaylistEntity(
            id = 0,
            name = playlist.name,
            description = playlist.description,
            url = playlist.coverUri.toString()
        )
    }

}