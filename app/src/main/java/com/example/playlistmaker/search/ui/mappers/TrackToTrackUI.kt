package com.example.playlistmaker.search.ui.mappers

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.models.TrackUI

object TrackToTrackUI {
    fun fromTrack(track: Track): TrackUI {
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