package com.example.playlistmaker.favoritetracks.data.db.converter

import com.example.playlistmaker.favoritetracks.data.db.entity.TrackEntity
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.models.Track

class TrackDbConverter {
    fun map(trackDto: TrackDto): TrackEntity {
        return TrackEntity(
            trackId = trackDto.trackId,
            trackName = trackDto.getParam(trackDto.trackName),
            artistName = trackDto.getParam(trackDto.artistName),
            trackTime = trackDto.getTrackTime(),
            artworkUrl100 = trackDto.getParam(trackDto.artworkUrl100),
            country = trackDto.getParam(trackDto.country),
            collectionName = trackDto.getParam(trackDto.collectionName),
            year = trackDto.getYear(),
            previewUrl = trackDto.getCoverArtwork(),
            primaryGenreName = trackDto.getParam(trackDto.primaryGenreName),
            coverArtWork = trackDto.getCoverArtwork()
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
            coverArtWork = trackEntity.coverArtWork
        )
    }

}