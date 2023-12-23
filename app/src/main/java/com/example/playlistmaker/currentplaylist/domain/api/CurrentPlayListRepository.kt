package com.example.playlistmaker.currentplaylist.domain.api

import com.example.playlistmaker.playlist.domain.models.PlayList
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface CurrentPlayListRepository {
    suspend fun getCurrentPlayListTracks(tracksId: List<Long>): Flow<List<Track>>

    suspend fun getPlayListById(id: Int): PlayList

    suspend fun updatePlaiListIdsInTrack(track: Track, playListID: Int)

    suspend fun getPlayListIdsForTrack(id: Long): ArrayList<Int>

    suspend fun deleteTrackFromDB(track: Track, playList: PlayList)

    suspend fun getSharingMessage(playListID: Int): String

    suspend fun deletePlayListFromBD(playList: PlayList)
}