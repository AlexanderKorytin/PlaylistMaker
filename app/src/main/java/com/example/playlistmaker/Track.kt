package com.example.playlistmaker

import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,
    val country: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String
){
    fun getTrackTime(): String {
        return if (trackTimeMillis!=null) SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis.toLong())
        else trackTimeMillis
    }
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")

    fun getYear() = if(releaseDate!=null)releaseDate.substring(0,4) else ""
}