package com.example.playlistmaker.currentplaylist.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.currentplaylist.data.db.entity.TrackPLEntity

@Dao
interface AllTracksDao {
    @Insert(TrackPLEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(trackPLEntity: TrackPLEntity)

    @Query("SELECT * FROM all_tracks WHERE trackId IN (:tracksIds)")
    suspend fun getTracksByIds(tracksIds: List<Long>): List<TrackPLEntity>
}