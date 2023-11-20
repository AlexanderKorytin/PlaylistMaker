package com.example.playlistmaker.favoritetracks.domain.impl

import com.example.playlistmaker.favoritetracks.domain.api.FavoriteTracksDatabaseRepository
import com.example.playlistmaker.favoritetracks.domain.api.FavoriteTracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(
    private val favoriteTracksDatabaseRepository: FavoriteTracksDatabaseRepository
) : FavoriteTracksInteractor {
    override suspend fun changeSignFavorite(track: Track) {
        if (track.inFavorite) favoriteTracksDatabaseRepository.deleteTrack(track = track)
        else favoriteTracksDatabaseRepository.insertNewTrack(track = track)
    }

    override suspend fun getFavoriteTracks(): Flow<List<Track>> {
        return favoriteTracksDatabaseRepository.getFavoriteTracks()
    }

}