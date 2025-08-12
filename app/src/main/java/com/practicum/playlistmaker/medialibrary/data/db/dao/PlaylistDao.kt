package com.practicum.playlistmaker.medialibrary.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
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
    @Query("SELECT * FROM playlist_table WHERE id = :playlistId")
    suspend fun getPlayListWithTracks(playlistId: Long): PlaylistWithTracks

    @Query("SELECT * FROM playlist_table")
    suspend fun getAllPlayListsWithTracks(): List<PlaylistWithTracks>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrack(crossRef: PlaylistTrackCrossRef) : Long

    @Delete(entity = PlaylistTrackCrossRef::class)
    suspend fun removeTrackFromPlaylist(crossRef: PlaylistTrackCrossRef)

}


