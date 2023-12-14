package com.example.playlistmaker.currentplaylist.data.db.converter

import com.example.playlistmaker.currentplaylist.data.db.entity.CurrentPlayListTrackEntity
import com.example.playlistmaker.playlist.domain.models.PlayList
import com.example.playlistmaker.search.domain.models.Track

class CurrentPlayListTrackDBConverter {
    fun map(track: Track, playList: PlayList): CurrentPlayListTrackEntity {
        return CurrentPlayListTrackEntity(
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
            playListId = playList.playListId,
        )
    }

    fun map(currentPlayListTrackEntity: CurrentPlayListTrackEntity): Track {
        return Track(
            trackId = currentPlayListTrackEntity.trackId,
            trackName = currentPlayListTrackEntity.trackName,
            artistName = currentPlayListTrackEntity.artistName,
            trackTime = currentPlayListTrackEntity.trackTime,
            artworkUrl100 = currentPlayListTrackEntity.artworkUrl100,
            country = currentPlayListTrackEntity.country,
            collectionName = currentPlayListTrackEntity.collectionName,
            year = currentPlayListTrackEntity.year,
            primaryGenreName = currentPlayListTrackEntity.primaryGenreName,
            previewUrl = currentPlayListTrackEntity.previewUrl,
            coverArtWork = currentPlayListTrackEntity.coverArtWork,
        )
    }
}
