package com.example.playlistmaker.favoritetracks.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.favoritetracks.data.db.entity.TrackEntity

@Dao
interface FavoriteTracksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewTrack(trackEntiy: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrackFormFavorites(track: TrackEntity)

    @Query("SELECT trackId FROM 'favorite_tracks.db' WHERE trackId = :trackId")
    suspend fun getIdFavoriteTracks(trackId: Long):List<Long>

    @Query("SELECT * FROM `favorite_tracks.db`")
    suspend fun getFavoriteTracks(): List<TrackEntity>
}