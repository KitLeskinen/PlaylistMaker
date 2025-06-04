package com.practicum.playlistmaker.medialibrary.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tracks_table")
data class TrackEntity(
    @PrimaryKey
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: Long,
    val artworkUrl100: String,
    val country: String,
    val primaryGenreName: String,
    val releaseDate: String,
    val collectionName: String,
    val previewUrl: String,
    val favoritedAt: Long
)