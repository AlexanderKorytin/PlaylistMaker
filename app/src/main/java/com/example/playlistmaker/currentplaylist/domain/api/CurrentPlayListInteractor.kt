package com.example.playlistmaker.currentplaylist.domain.api

import com.example.playlistmaker.playlist.domain.models.PlayList
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface CurrentPlayListInteractor {
    suspend fun getTracksFromPlailist(playListId: Int): Flow<List<Track>>

    suspend fun saveTrackToPlayList(track: Track, playList: PlayList)
}