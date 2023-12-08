package com.example.playlistmaker.playlist.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson

@Entity(tableName = "Playlists")
data class PlayListEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("playListId")
    var playListId: Int = 0,
    val playListName: String,
    val playListDescription: String,
    val playListCover: String,
    var tracksIds: String,
    var quantityTracks: Int
)