package com.example.playlistmaker.currentplaylist.domain.api

import com.example.playlistmaker.playlist.domain.models.PlayList
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface CurrentPlayListRepository {
    suspend fun getCurrentPlayListTracks(playListId: Int): Flow<List<Track>>
    suspend fun saveTrackToPlayList(track: Track ,playList: PlayList)
}