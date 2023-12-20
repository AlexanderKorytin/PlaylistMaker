package com.example.playlistmaker.playlist.domain.api

import com.example.playlistmaker.playlist.domain.models.PlayList
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlayListInteractor {
    suspend fun saveTrackIdsToPlayListToDb(playList: PlayList)

    suspend fun getAllPlayLists(): Flow<List<PlayList>>

    suspend fun saveTrack(track: Track, playList: PlayList)

    suspend fun savePlayList(playList: PlayList)
}