package com.example.playlistmaker.playlist.domain.api

import com.example.playlistmaker.playlist.domain.models.PlayList
import kotlinx.coroutines.flow.Flow

interface PlayListInteractor {
    suspend fun savePlayListToDb(playList: PlayList)

    suspend fun getAllPlayLists(): Flow<List<PlayList>>
}