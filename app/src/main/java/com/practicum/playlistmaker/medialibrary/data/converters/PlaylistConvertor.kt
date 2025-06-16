package com.practicum.playlistmaker.medialibrary.data.converters

import android.net.Uri
import com.practicum.playlistmaker.common.data.domain.entity.Playlist
import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistEntity


class PlaylistDbConvertor {

    fun map(playlist: Playlist): PlaylistEntity{
        return PlaylistEntity(
            id = 0,
            name = playlist.name,
            description = playlist.description,
            url = playlist.coverUri.toString()
        )
    }

    fun map(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            name = playlistEntity.name,
            description = playlistEntity.description,
            coverUri = Uri.parse(playlistEntity.url),
            trackList = null
        )
    }
}