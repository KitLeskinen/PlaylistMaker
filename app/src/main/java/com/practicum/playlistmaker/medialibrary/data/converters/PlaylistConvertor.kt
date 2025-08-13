package com.practicum.playlistmaker.medialibrary.data.converters

import com.practicum.playlistmaker.common.data.domain.entity.Playlist
import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistEntity


class PlaylistDbConvertor {

    fun map(playlist: Playlist, replace: Boolean): PlaylistEntity{
        var id = 0L
        if(replace){
            id = playlist.id
        }
        return PlaylistEntity(
            id = id,
            name = playlist.name,
            description = playlist.description,
            url = playlist.coverUri ?: ""
        )
    }

    fun map(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            id = playlistEntity.id,
            name = playlistEntity.name,
            description = playlistEntity.description,
            coverUri = playlistEntity.url,
            trackList = null
        )
    }
}