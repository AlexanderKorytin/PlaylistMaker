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

    override suspend fun saveTrackPlayListIdsChange(track: Track, playListId: Int) {
        currentPlayListRepository.updatePlaiListIdsInTrack(track, playListId)
    }

    override suspend fun getPlayListIdsCurrentTrack(tracksId: Long): ArrayList<Int> {
        return currentPlayListRepository.getPLIdFromTrack(tracksId)
    }

    override suspend fun deleteTrackFromPlayList(track: Track, playList: PlayList) {
        currentPlayListRepository.deleteTrackFromPlayList(track, playList)
    }

    override fun shareTrackList(message: String) {
        currentPlayListRepository.shareTrackList(message)
    }


}