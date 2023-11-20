package com.example.playlistmaker.favoritetracks.domain

import com.example.playlistmaker.favoritetracks.data.db.entity.TrackEntity
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow


interface FavoriteTracksDatabaseRepository {

    suspend fun insertNewTrack(trackEntity: TrackEntity)

    suspend fun deleteTrack(trackEntity: TrackEntity)

    fun getIdFavoriteTracks(trackId: Long): Flow<List<Long>>

    fun getFavoriteTracks(): Flow<List<Track>>
}