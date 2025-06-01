package com.practicum.playlistmaker.medialibrary.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.medialibrary.data.db.entity.TrackEntity

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: List<TrackEntity>)

    @Query("SELECT * FROM tracks_table")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT * FROM tracks_table WHERE trackId = :trackId")
    suspend fun getTrack(trackId: Long): List<TrackEntity>

    @Delete
    suspend fun delete(track: TrackEntity)

}