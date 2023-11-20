package com.example.playlistmaker.favoritetracks.data.db.repository

import com.example.playlistmaker.favoritetracks.data.db.AppDataBase
import com.example.playlistmaker.favoritetracks.data.db.converter.TrackDbConverter
import com.example.playlistmaker.favoritetracks.data.db.entity.TrackEntity
import com.example.playlistmaker.favoritetracks.domain.FavoriteTracksDatabaseRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksDatabaseRepositoryImpl(
    private val appDatabase: AppDataBase,
    private val converter: TrackDbConverter
) : FavoriteTracksDatabaseRepository {
    override suspend fun insertNewTrack(trackEntity: TrackEntity) {
        appDatabase.getFavoriteTrackDao().insertNewTrack(trackEntity)
    }

    override suspend fun deleteTrack(trackEntity: TrackEntity) {
        appDatabase.getFavoriteTrackDao().deleteTrackFormFavorites(trackEntity)
    }

    override fun getIdFavoriteTracks(trackId: Long): Flow<List<Long>> = flow {
        val ids = appDatabase.getFavoriteTrackDao().getIdFavoriteTracks(trackId)
        emit(ids)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.getFavoriteTrackDao().getFavoriteTracks()
        emit(tracks.map { track -> converter.map(track) })
    }
}
