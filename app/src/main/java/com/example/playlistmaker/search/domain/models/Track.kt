package com.example.playlistmaker.search.domain.models


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
    val coverArtWork: String,
    var inFavorite: Boolean = false,
    var playListIds: ArrayList<Int>
)