package com.example.playlistmaker.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.playlist.data.db.dao.PlaylistsBaseDao
import com.example.playlistmaker.playlist.data.db.entity.PlayListEntity
import com.example.playlistmaker.favorite.data.db.dao.FavoriteTracksDao
import com.example.playlistmaker.favorite.data.db.entity.TrackEntity

@Database(entities = [TrackEntity::class , PlayListEntity::class], version = 1,)
abstract class AppDataBase: RoomDatabase() {
    abstract fun getFavoriteTrackDao(): FavoriteTracksDao
    abstract fun getPlayListsBaseDao(): PlaylistsBaseDao

}