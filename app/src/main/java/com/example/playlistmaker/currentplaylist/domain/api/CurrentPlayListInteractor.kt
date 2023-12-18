package com.example.playlistmaker.currentplaylist.domain.api

import com.example.playlistmaker.playlist.domain.models.PlayList
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface CurrentPlayListInteractor {
    suspend fun getTracksFromPlailist(tracksId: List<Long>): Flow<List<Track>>

    suspend fun getPlayListById(playListId: Int): PlayList

    suspend fun saveTrackPlayListIdsChange(track: Track, playListId: Int)

    suspend fun getPlayListIdsCurrentTrack(tracksId: Long): ArrayList<Int>

    suspend fun deleteTrackFromPlayList(track: Track, playList: PlayList)

    fun shareTrackList(message: String)
}