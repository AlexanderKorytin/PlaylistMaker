package com.example.playlistmaker.favoritetracks.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.Calendar

@Entity(tableName = "favorite_tracks")
data class TrackEntity(
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
    val insertignTime: Long = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
)
