package com.example.playlistmaker.presentation.mappers

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.models.TrackUI

class MapToTrackUI {
    fun mapList(track: List<Track>?): ArrayList<TrackUI> {
        return track?.map { it -> TrackUI(
            it.trackId,
            it.trackName,
            it.artistName,
            it.trackTime,
            it.artworkUrl100,
            it.country,
            it.collectionName,
            it.year,
            it.primaryGenreName,
            it.previewUrl,
            it.coverArtWork
        ) } as ArrayList<TrackUI>
    }

}