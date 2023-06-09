package com.example.playlistmaker

import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String
){
    fun getTrackTime(): String {
        return if (trackTimeMillis!=null) SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis.toLong())
        else trackTimeMillis
    }
}