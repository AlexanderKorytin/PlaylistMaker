package com.example.playlistmaker.playlist.domain.impl

import com.example.playlistmaker.playlist.domain.api.PlayListInteractor
import com.example.playlistmaker.playlist.domain.api.PlayListsRepository
import com.example.playlistmaker.playlist.domain.models.PlayList
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlayListInteractorImpl(private val repository: PlayListsRepository) : PlayListInteractor {
    override suspend fun saveTrackIdsToPlayListToDb(playList: PlayList) {
        repository.savePlayListToBase(playList)
    }

    override suspend fun getAllPlayLists(): Flow<List<PlayList>> {
        return repository.getAllPlayLists()
    }

    override suspend fun saveTrack(track: Track, playList: PlayList) {
        repository.saveTrack(track, playList)
    }

    override suspend fun savePlayList(playList: PlayList) {
        repository.savePlayList(playList)
    }

}