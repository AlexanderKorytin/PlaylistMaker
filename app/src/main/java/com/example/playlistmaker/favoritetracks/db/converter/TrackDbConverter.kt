package com.example.playlistmaker.favoritetracks.db.converter

import com.example.playlistmaker.favoritetracks.db.entity.TrackEntity
import com.example.playlistmaker.search.domain.models.Track

class TrackDbConverter {
    fun map(track: Track): TrackEntity {
        return TrackEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = track.trackTime,
            artworkUrl100 = track.artworkUrl100,
            country = track.country,
            collectionName = track.collectionName,
            year = track.year,
            previewUrl = track.previewUrl,
            primaryGenreName = track.primaryGenreName,
            coverArtWork = track.coverArtWork,
        )
    }

    fun map(trackEntity: TrackEntity): Track {
        return Track(
            trackId = trackEntity.trackId,
            trackName = trackEntity.trackName,
            artistName = trackEntity.artistName,
            trackTime = trackEntity.trackTime,
            artworkUrl100 = trackEntity.artworkUrl100,
            country = trackEntity.country,
            collectionName = trackEntity.collectionName,
            year = trackEntity.year,
            primaryGenreName = trackEntity.primaryGenreName,
            previewUrl = trackEntity.previewUrl,
            coverArtWork = trackEntity.coverArtWork,
            inFavorite = true
        )
    }

}