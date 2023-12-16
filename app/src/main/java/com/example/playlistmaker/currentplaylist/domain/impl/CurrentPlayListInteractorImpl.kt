package com.example.playlistmaker.currentplaylist.domain.impl

import com.example.playlistmaker.currentplaylist.domain.api.CurrentPlayListInteractor
import com.example.playlistmaker.currentplaylist.domain.api.CurrentPlayListRepository
import com.example.playlistmaker.playlist.domain.models.PlayList
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class CurrentPlayListInteractorImpl(
    private val currentPlayListRepository: CurrentPlayListRepository
) : CurrentPlayListInteractor {
    override suspend fun getTracksFromPlailist(tracksId: List<Long>): Flow<List<Track>> {
        return currentPlayListRepository.getCurrentPlayListTracks(tracksId)
    }

    override suspend fun getPlayListById(playListId: Int): PlayList {
       return currentPlayListRepository.getPlayListById(playListId)
    }


}