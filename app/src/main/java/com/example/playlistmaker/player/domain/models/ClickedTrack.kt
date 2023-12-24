package com.example.playlistmaker.player.domain.models

import com.example.playlistmaker.search.domain.models.Track

data class ClickedTrack(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val country: String,
    val artworkUrl100: String,
    val collectionName: String,
    val year: String,
    val primaryGenreName: String,
    val previewUrl: String,
    val coverArtWork: String,
    val inFavorite: Boolean,
    val playListIds: MutableList<Int>
) {
    fun mapClickedTrackToTrack(): Track {
        return Track(
            trackId = trackId,
            trackName = trackName,
            artistName = artistName,
            trackTime = trackTime,
            country = country,
            collectionName = collectionName,
            year = year,
            primaryGenreName = primaryGenreName,
            previewUrl = previewUrl,
            coverArtWork = coverArtWork,
            inFavorite = inFavorite,
            artworkUrl100 = artworkUrl100,
            playListIds = playListIds as ArrayList<Int>
        )
    }
}