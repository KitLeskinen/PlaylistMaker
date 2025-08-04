package com.practicum.playlistmaker.medialibrary.data.db.dao

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistTrackCrossRef
import com.practicum.playlistmaker.medialibrary.data.db.entity.TrackEntity

data class PlaylistWithTracks(
    @Embedded val playlist: PlaylistEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "trackId",
        associateBy = Junction(
            value = PlaylistTrackCrossRef::class,
            parentColumn = "playlistId",
            entityColumn = "trackId"
        )
    )

    val tracks: List<TrackEntity>
)