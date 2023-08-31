package com.example.playlistmaker.presentation.mappers

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.models.TrackUI

class MapToTrackUI {
    fun map(track: Track): TrackUI {
        return TrackUI(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.country,
            track.collectionName,
            track.year,
            track.primaryGenreName,
            track.previewUrl,
            track.coverArtWork
        )
    }
}