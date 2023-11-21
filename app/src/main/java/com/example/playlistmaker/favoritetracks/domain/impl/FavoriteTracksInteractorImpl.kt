package com.example.playlistmaker.favoritetracks.domain.impl

import com.example.playlistmaker.favoritetracks.domain.api.FavoriteTracksDatabaseRepository
import com.example.playlistmaker.favoritetracks.domain.api.FavoriteTracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

class FavoriteTracksInteractorImpl(
    private val favoriteTracksDatabaseRepository: FavoriteTracksDatabaseRepository
) : FavoriteTracksInteractor {
    override suspend fun changeSignFavorite(track: Track) {
        var listId: List<Long> = emptyList()
        withContext(Dispatchers.IO){
            favoriteTracksDatabaseRepository.getIdFavoriteTracks().collect{
                listId = it
            }
        }
        if (listId.contains(track.trackId)) {
            favoriteTracksDatabaseRepository.deleteTrack(track = track)
        }
        else {
            favoriteTracksDatabaseRepository.insertNewTrack(track = track)
        }
    }

    override suspend fun getFavoriteTracks(): Flow<List<Track>> {
        return favoriteTracksDatabaseRepository.getFavoriteTracks()
    }

}