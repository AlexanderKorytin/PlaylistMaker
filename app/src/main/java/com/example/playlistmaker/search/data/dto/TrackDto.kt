package com.example.playlistmaker.search.data.dto

import java.text.SimpleDateFormat
import java.util.Locale

data class TrackDto(
    val trackId: Long,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: String?,
    val artworkUrl100: String?,
    val country: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val previewUrl: String?
) {
    fun getTrackTime(): String {
        return if (trackTimeMillis != null) SimpleDateFormat("mm:ss", Locale.getDefault()).format(
            trackTimeMillis.toLong()
        )
        else ""
    }

    fun getCoverArtwork() = if(artworkUrl100!=null) artworkUrl100.replaceAfterLast('/', "512x512bb.jpg") else ""

    fun getYear() = if (releaseDate != null) releaseDate.substring(0, 4) else ""

    fun getParam(param: String?) = if(param!=null) param else ""
}
