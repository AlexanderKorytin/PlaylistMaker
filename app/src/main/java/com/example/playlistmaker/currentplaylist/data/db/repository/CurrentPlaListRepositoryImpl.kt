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
        val listTrack = appDataBase.getCurrentPlayListDao().getTracksCurrentList(playListId)
            .map { currentPlayListTrackDBConverter.map(it) }
        val listId = appDataBase.getFavoriteTrackDao().getIdFavoriteTracks()
        val resultList = listTrack.map {
            Track(
                it.trackId,
                it.trackName,
                it.artistName,
                it.trackTime,
                it.artworkUrl100,
                it.country,
                it.collectionName,
                it.year,
                it.primaryGenreName,
                it.previewUrl,
                it.coverArtWork,
                inFavorite = listId.contains(it.trackId)
            )
        }
        emit(resultList)
    }

    override suspend fun saveTrackToPlayList(track: Track, playList: PlayList) {
        appDataBase.getCurrentPlayListDao()
            .saveTrackToPlaylistDB(currentPlayListTrackDBConverter.map(track, playList))
    }

}