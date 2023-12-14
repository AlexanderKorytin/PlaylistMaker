package com.example.playlistmaker.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.currentplaylist.data.db.dao.CurrentPlayListDao
import com.example.playlistmaker.currentplaylist.data.db.entity.CurrentPlayListTrackEntity
import com.example.playlistmaker.favoritetracks.db.dao.FavoriteTracksDao
import com.example.playlistmaker.favoritetracks.db.entity.TrackEntity
import com.example.playlistmaker.playlist.data.db.dao.AllTracksDao
import com.example.playlistmaker.playlist.data.db.dao.PlaylistsBaseDao
import com.example.playlistmaker.playlist.data.db.entity.PlayListEntity
import com.example.playlistmaker.playlist.data.db.entity.TrackPLEntity

@Database(entities = [TrackEntity::class, PlayListEntity::class, TrackPLEntity::class, CurrentPlayListTrackEntity::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun getFavoriteTrackDao(): FavoriteTracksDao
    abstract fun getPlayListsBaseDao(): PlaylistsBaseDao
    abstract fun getAllTracksDao(): AllTracksDao
    abstract fun getCurrentPlayListDao(): CurrentPlayListDao

}