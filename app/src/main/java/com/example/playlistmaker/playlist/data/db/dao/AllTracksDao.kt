package com.example.playlistmaker.playlist.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.playlistmaker.playlist.data.db.entity.TrackPLEntity

@Dao
interface AllTracksDao {
@Insert(TrackPLEntity::class,  onConflict = OnConflictStrategy.IGNORE)
suspend fun insertTrack(trackPLEntity: TrackPLEntity)
}