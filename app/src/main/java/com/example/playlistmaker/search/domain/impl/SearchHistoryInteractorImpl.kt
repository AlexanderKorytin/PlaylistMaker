package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class SearchHistoryInteractorImpl(val searchHistoryRepository: SearchHistoryRepository) :
    SearchHistoryInteractor {
    override fun getTracksList(): Flow<ArrayList<Track>> =
        searchHistoryRepository.getSearchHistoryList()


    override fun clear() {
        searchHistoryRepository.clearHistory()
    }

    override fun saved(track: Track) {
        searchHistoryRepository.savedTrack(track)
    }

}