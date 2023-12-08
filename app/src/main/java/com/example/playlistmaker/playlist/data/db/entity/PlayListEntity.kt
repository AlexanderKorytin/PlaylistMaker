package com.example.playlistmaker.playlist.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.Nullable
import java.io.File

@Entity(tableName = "Playlists")
data class PlayListEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("playListId")
    var playListId: Int = 0,
    @ColumnInfo("playListName")
    val playListName: String,
    val playListDescription: String,
    var playListCover: String,
    var tracksIds: String,
    var quantityTracks: Int
    )