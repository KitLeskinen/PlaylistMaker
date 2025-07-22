package com.practicum.playlistmaker.medialibrary.data.db.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistTrackCrossRef


@Dao
interface PlaylistDao {

    @Query("SELECT * from playlist_table")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT COUNT(*) FROM playlists_tracks WHERE playlistId = :playlistId")
    suspend fun getTrackCountFromPlaylist(playlistId: Long): Int

    @Transaction
    @Query("SELECT * FROM playlist_table where id = :playlistId")
    suspend fun getPlayListWithTracks(playlistId: Long) : PlaylistWithTracks

    @Query("SELECT * FROM playlist_table")
    suspend fun getAllPlayListsWithTracks() : List<PlaylistWithTracks>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrack(crossRef: PlaylistTrackCrossRef){
        Log.d("TAG", "addTrack: $crossRef")
    }


//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertTrack(track: List<TrackEntity>)
//
//    @Query("SELECT * FROM tracks_table WHERE favoritedAt not NULL ORDER BY favoritedAt ASC")
//    suspend fun getFavoriteTracks(): List<TrackEntity>
//
//    @Query("SELECT * FROM tracks_table WHERE trackId = :trackId")
//    suspend fun getTrack(trackId: Long): List<TrackEntity>
//
//    @Delete
//    suspend fun delete(track: TrackEntity)

}


