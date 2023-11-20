package com.example.playlistmaker.favoritetracks.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tracks.db")
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
    val coverArtWork: String
)