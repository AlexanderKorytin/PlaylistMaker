package com.example.playlistmaker.data.mediaplayer.mapper

import com.example.playlistmaker.data.dto.TrackUrl
import com.example.playlistmaker.domain.models.ClickedTrack

class MediaPlayerMapper {
    fun toMediaPlayer(clickedTrack: ClickedTrack): TrackUrl {
        return TrackUrl(clickedTrack.previewUrl)
    }
}