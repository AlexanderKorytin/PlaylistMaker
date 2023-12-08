package com.example.playlistmaker.playlist.domain.impl

import com.example.playlistmaker.playlist.domain.api.PlayListInteractor
import com.example.playlistmaker.playlist.domain.api.PlayListsRepository
import com.example.playlistmaker.playlist.domain.models.PlayList
import kotlinx.coroutines.flow.Flow

class PlayListInteractorImpl(private val repository: PlayListsRepository) : PlayListInteractor {
    override suspend fun savePlayListToDb(playList: PlayList) {
        repository.savePlayListToBase(playList)
    }

    override suspend fun getAllPlayLists(): Flow<List<PlayList>> {
        return repository.getAllPlayLists()
    }

}