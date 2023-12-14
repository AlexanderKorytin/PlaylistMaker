package com.example.playlistmaker.currentplaylist.data.db.repository

import com.example.playlistmaker.core.db.AppDataBase
import com.example.playlistmaker.currentplaylist.data.db.converter.CurrentPlayListTrackDBConverter
import com.example.playlistmaker.currentplaylist.domain.api.CurrentPlayListRepository
import com.example.playlistmaker.playlist.domain.models.PlayList
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CurrentPlaListRepositoryImpl(
    private val currentPlayListTrackDBConverter: CurrentPlayListTrackDBConverter,
    private val appDataBase: AppDataBase
) : CurrentPlayListRepository {
    override suspend fun getCurrentPlayListTracks(playListId: Int): Flow<List<Track>> = flow {
        val list = appDataBase.getCurrentPlayListDao().getTracksCurrentList(playListId)
        emit(list.map { currentPlayListTrackDBConverter.map(it) })
    }

    override suspend fun saveTrackToPlayList(track: Track, playList: PlayList) {
        appDataBase.getCurrentPlayListDao().saveTrackToPlaylistDB(currentPlayListTrackDBConverter.map(track, playList))
    }

}