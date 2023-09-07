package com.example.playlistmaker.search.domain.models

import com.example.playlistmaker.search.ui.models.TrackUI


data class Track(
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
){
    fun toTrackUI(): TrackUI {
        return TrackUI(
            trackId,
            trackName,
            artistName,
            trackTime,
            artworkUrl100,
            country,
            collectionName,
            year,
            primaryGenreName,
            previewUrl,
            coverArtWork
        )
    }
}