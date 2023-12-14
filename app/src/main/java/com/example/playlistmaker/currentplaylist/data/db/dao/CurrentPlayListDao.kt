package com.example.playlistmaker.currentplaylist.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.currentplaylist.data.db.entity.CurrentPlayListTrackEntity

@Dao
interface CurrentPlayListDao {
    @Query("SELECT * FROM playlists_track WHERE playListId = :playListId")
    suspend fun getTracksCurrentList(playListId: Int): List<CurrentPlayListTrackEntity>

    @Insert(CurrentPlayListTrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTrackToPlaylistDB(currentPlayListTrackEntity: CurrentPlayListTrackEntity)
}