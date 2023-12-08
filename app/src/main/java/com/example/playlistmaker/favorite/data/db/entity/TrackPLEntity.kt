package com.example.playlistmaker.favorite.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar


@Entity(tableName = "all_tracks")
data class TrackPLEntity(
    @PrimaryKey @ColumnInfo(name = "trackId")
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String,
    val country: String,
    val collectionName: String,
    val year: String,
    val primaryGenreName: String,
    val previewUrl: String,
    val coverArtWork: String,
    val insertignTime: Long = Calendar.getInstance().time.time
)
