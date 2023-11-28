package com.example.playlistmaker.favorite.domain.api

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow


interface FavoriteTracksInteractor {

    suspend fun changeSignFavorite(track: Track)

    suspend fun getFavoriteTracks(): Flow<List<Track>>

}