package com.example.playlistmaker.favorite.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.favorite.data.db.dao.FavoriteTracksDao
import com.example.playlistmaker.favorite.data.db.entity.TrackEntity

@Database(entities = [TrackEntity::class ], version = 1,)
abstract class AppDataBase: RoomDatabase() {
    abstract fun getFavoriteTrackDao(): FavoriteTracksDao
}