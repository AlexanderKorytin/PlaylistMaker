package com.example.playlistmaker.currentplaylist.data.db.repository

import com.example.playlistmaker.core.db.AppDataBase
import com.example.playlistmaker.currentplaylist.domain.api.CurrentPlayListRepository
import com.example.playlistmaker.playlist.data.db.converter.PlayListDbConverter
import com.example.playlistmaker.playlist.domain.models.PlayList
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CurrentPlaListRepositoryImpl(
    private val currentPlayListTrackDBConverter: PlayListDbConverter,
    private val appDataBase: AppDataBase,
    private val json: Gson
) : CurrentPlayListRepository {
    override suspend fun getCurrentPlayListTracks(tracksIds: List<Long>): Flow<List<Track>> = flow {
        val listTrack = appDataBase.getAllTracksDao().getTracksByIds(tracksIds.map { it.toLong() })
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
                inFavorite = listId.contains(it.trackId),
                it.playListIds
            )
        }
        emit(resultList)
    }

    override suspend fun getPlayListById(id: Int): PlayList {
        val currentList = appDataBase.getPlayListsBaseDao().getPlayListById(id)
        return currentPlayListTrackDBConverter.map(currentList)
    }

    override suspend fun updatePlaiListIdsInTrack(track: Track, playListID: Int) {
        if (!track.playListIds.contains(playListID)){
            track.playListIds.add(playListID)
        }
        appDataBase.getAllTracksDao().updateTrack(currentPlayListTrackDBConverter.map(track))

    }

    override suspend fun getPLIdFromTrack(trackId: Long): ArrayList<Int> {
        val result = appDataBase.getAllTracksDao().getPlayListIDFromTrack(trackId)
        return if (result != null) json.fromJson(result, object : TypeToken<ArrayList<Int>>(){}.type)
        else arrayListOf()
    }

    override suspend fun deleteTrackFromPlayList(track: Track, playList: PlayList) {
        val listId = getPLIdFromTrack(track.trackId)
        if (listId.size == 1) {
            appDataBase.getAllTracksDao().deleteTrack(currentPlayListTrackDBConverter.map(track))
        }
        else {
            listId.remove(playList.playListId)
            track.playListIds = listId
            appDataBase.getAllTracksDao().updateTrack(currentPlayListTrackDBConverter.map(track))
        }
        playList.tracksIds.remove(track.trackId)
        playList.quantityTracks = playList.tracksIds.size
        appDataBase.getPlayListsBaseDao().updateTracksIdsInPlayList(currentPlayListTrackDBConverter.map(playList))
    }


}