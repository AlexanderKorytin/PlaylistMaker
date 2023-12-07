package com.example.playlistmaker.playlist.data.db.converter

import com.example.playlistmaker.playlist.data.db.entity.PlayListEntity
import com.example.playlistmaker.playlist.domain.models.PlayList

class PlayListDbConverter {
    fun map(playListEntity: PlayListEntity): PlayList {
        return PlayList(
            playListId = playListEntity.playListId,
            playListName = playListEntity.playListName,
            playListCover = playListEntity.playListCover,
            tracksIds = playListEntity.tracksIds,
            quantityTracks = playListEntity.quantityTracks,
            playListDescription = playListEntity.playListDescription
        )
    }

    fun map(playList: PlayList): PlayListEntity {
        return PlayListEntity(
            playListName = playList.playListName,
            playListCover = playList.playListCover,
            tracksIds = playList.tracksIds,
            quantityTracks = playList.quantityTracks,
            playListDescription = playList.playListDescription,
        )
    }
}