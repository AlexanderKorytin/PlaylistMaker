package com.example.playlistmaker.favoritetracks.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.favoritetracks.db.entity.TrackEntity

@Dao
interface FavoriteTracksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewTrack(trackEntiy: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrackFormFavorites(track: TrackEntity)

    @Query("SELECT trackId FROM 'favorite_tracks'")
    suspend fun getIdFavoriteTracks(): List<Long>

    @Query("SELECT * FROM `favorite_tracks` ORDER BY insertignTime DESC")
    suspend fun getFavoriteTracks(): List<TrackEntity>
}