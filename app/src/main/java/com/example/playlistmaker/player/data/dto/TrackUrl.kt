package com.example.playlistmaker.player.data.dto

import com.example.playlistmaker.player.domain.models.ClickedTrack

data class TrackUrl(val url: String?)

fun ClickedTrack.toMediaPlayer(): TrackUrl {
    return TrackUrl(this.previewUrl)
}