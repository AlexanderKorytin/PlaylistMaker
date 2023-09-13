package com.example.playlistmaker.player.ui.mappers

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.models.TrackUI

class MapToTrackUI {
    fun mapList(track: List<Track>?): ArrayList<TrackUI> {
        return track?.map { it ->
            TrackUI(
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
            )
        }as ArrayList<TrackUI>
    }

}