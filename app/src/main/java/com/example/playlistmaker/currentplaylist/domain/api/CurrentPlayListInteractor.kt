package com.example.playlistmaker.currentplaylist.domain.api

import com.example.playlistmaker.playlist.domain.models.PlayList
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface CurrentPlayListInteractor {
    suspend fun getTracksFromPlailist(tracksId: List<Long>): Flow<List<Track>>

    suspend fun getPlayListById(playListId: Int): PlayList
}