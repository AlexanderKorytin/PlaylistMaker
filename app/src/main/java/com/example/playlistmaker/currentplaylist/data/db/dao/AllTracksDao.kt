package com.example.playlistmaker.currentplaylist.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.currentplaylist.data.db.entity.TrackPLEntity

@Dao
interface AllTracksDao {
    @Insert(TrackPLEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(trackPLEntity: TrackPLEntity)

    @Query("SELECT * FROM all_tracks WHERE trackId IN (:tracksIds)")
    suspend fun getTracksByIds(tracksIds: List<Long>): List<TrackPLEntity>

    @Update(TrackPLEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTrack(trackPLEntity: TrackPLEntity)

    @Query("SELECT playlistIds FROM all_tracks WHERE trackId = :trackPLEntityId")
    suspend fun getPlayListIDFromTrack(trackPLEntityId: Long): String

    @Delete(TrackPLEntity::class)
    suspend fun deleteTrack(trackPLEntity: TrackPLEntity)
}