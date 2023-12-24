package com.example.playlistmaker.search.ui.models

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.search.domain.models.Track

data class TrackUI
    (
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
    var inFavorite: Boolean,
    var playlistId: MutableList<Int>
) {
    fun toTrack(): Track {
        return Track(
            trackId = trackId,
            trackName = trackName,
            artistName = artistName,
            trackTime = trackTime,
            artworkUrl100 = artworkUrl100,
            country = country,
            collectionName = collectionName,
            year = year,
            primaryGenreName = primaryGenreName,
            previewUrl = previewUrl,
            coverArtWork = coverArtWork,
            inFavorite = inFavorite,
            playListIds = playlistId as ArrayList<Int>
        )
    }
}