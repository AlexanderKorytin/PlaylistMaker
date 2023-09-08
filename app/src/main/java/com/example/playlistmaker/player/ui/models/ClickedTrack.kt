package com.example.playlistmaker.player.ui.models

import com.example.playlistmaker.player.data.dto.TrackUrl

data class ClickedTrack(
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val country: String,
    val collectionName: String,
    val year: String,
    val primaryGenreName: String,
    val previewUrl: String,
    val coverArtWork: String
) {
    fun toMediaPlayer(): TrackUrl {
        return TrackUrl(previewUrl)
    }
}