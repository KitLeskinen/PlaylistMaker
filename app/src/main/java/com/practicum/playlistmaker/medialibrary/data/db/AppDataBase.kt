package com.practicum.playlistmaker.medialibrary.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.medialibrary.data.db.dao.TrackDao
import com.practicum.playlistmaker.medialibrary.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDataBase : RoomDatabase(){
    abstract fun trackDao(): TrackDao
}