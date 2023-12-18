package com.example.playlistmaker.playlist.data.db.converter

import com.example.playlistmaker.currentplaylist.data.db.entity.TrackPLEntity
import com.example.playlistmaker.playlist.data.db.entity.PlayListEntity
import com.example.playlistmaker.playlist.domain.models.PlayList
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlayListDbConverter(private val json: Gson) {
    fun map(playListEntity: PlayListEntity): PlayList {
        return PlayList(
            playListId = playListEntity.playListId,
            playListName = playListEntity.playListName,
            playListCover = playListEntity.playListCover,
            tracksIds = json.fromJson(
                playListEntity.tracksIds,
                object : TypeToken<ArrayList<Long>>() {}.type
            ),
            quantityTracks = playListEntity.quantityTracks,
            playListDescription = playListEntity.playListDescription
        )
    }

    fun map(playList: PlayList): PlayListEntity {
        return PlayListEntity(
            playListId = playList.playListId,
            playListName = playList.playListName,
            playListCover = playList.playListCover,
            tracksIds = json.toJson(playList.tracksIds),
            quantityTracks = playList.quantityTracks,
            playListDescription = playList.playListDescription,
        )
    }

    fun map(track: Track): TrackPLEntity {
        return TrackPLEntity(
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
            playlistIds = json.toJson(track.playListIds)
        )
    }

    fun map(trackEntity: TrackPLEntity): Track {
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
            inFavorite = true,
            playListIds = json.fromJson(
                trackEntity.playlistIds,
                object : TypeToken<ArrayList<Int>>() {}.type
            )
        )
    }

}