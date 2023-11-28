package com.example.playlistmaker.favorite.domain.api

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow


interface FavoriteTracksDatabaseRepository {

    suspend fun insertNewTrack(track: Track)

    suspend fun deleteTrack(track: Track)

    fun getIdFavoriteTracks(): Flow<List<Long>>

    fun getFavoriteTracks(): Flow<List<Track>>
}