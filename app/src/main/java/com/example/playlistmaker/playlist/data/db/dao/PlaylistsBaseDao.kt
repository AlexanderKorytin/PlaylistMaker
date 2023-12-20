package com.example.playlistmaker.playlist.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.playlist.data.db.entity.PlayListEntity

@Dao
interface PlaylistsBaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePlayList(playList: PlayListEntity)

    @Query("SELECT * FROM Playlists ")
    suspend fun getAllPlayLists(): List<PlayListEntity>

    @Query("SELECT * FROM playlists WHERE playListId = :id")
    suspend fun getPlayListById(id: Int): PlayListEntity

    @Query("SELECT tracksIds FROM Playlists WHERE playListId = :playListId")
    suspend fun getTracksIdsFromPlayList(playListId: Int): String

    @Update(entity = PlayListEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlayList(entity: PlayListEntity)

    @Delete(PlayListEntity::class)
    suspend fun deletePlayList(playList: PlayListEntity)
}