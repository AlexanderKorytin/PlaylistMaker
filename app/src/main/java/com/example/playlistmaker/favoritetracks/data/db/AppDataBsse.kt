package com.example.playlistmaker.favoritetracks.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.favoritetracks.data.db.dao.FavoriteTracksDao
import com.example.playlistmaker.favoritetracks.data.db.entity.TrackEntity

@Database(entities = [TrackEntity::class ], version = 1,)
abstract class AppDataBase: RoomDatabase() {
    abstract fun getFavoriteTrackDao(): FavoriteTracksDao
}