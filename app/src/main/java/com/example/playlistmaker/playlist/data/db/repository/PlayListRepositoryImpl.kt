package com.example.playlistmaker.playlist.data.db.repository

import com.example.playlistmaker.playlist.data.db.converter.PlayListDbConverter
import com.example.playlistmaker.playlist.domain.api.PlayListsRepository
import com.example.playlistmaker.playlist.domain.models.PlayList
import com.example.playlistmaker.core.db.AppDataBase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayListRepositoryImpl(
    private val appDataBase: AppDataBase,
    private val converter: PlayListDbConverter
) : PlayListsRepository {
    override suspend fun savePlayListToBase(playList: PlayList) {
       appDataBase.getPlayListsBaseDao().savePlayList(converter.map(playList))
    }

    override suspend fun getAllPlayLists(): Flow<List<PlayList>> = flow {
         val playLists = appDataBase.getPlayListsBaseDao().getAllPlayLists()
         emit(playLists.map { converter.map(it) })
    }

}