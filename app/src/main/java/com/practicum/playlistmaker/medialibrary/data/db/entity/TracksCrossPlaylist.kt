package com.practicum.playlistmaker.medialibrary.data.db.entity


import androidx.room.Entity


@Entity(tableName = "playlists_tracks", primaryKeys = ["playlistId", "trackId"])
data class PlaylistTrackCrossRef(
    val playlistId: Long,
    val trackId: Long
)