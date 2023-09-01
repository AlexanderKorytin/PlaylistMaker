package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.models.Track

class SearchHistoryInteractorImpl(val searchHistoryRepository: SearchHistoryRepository): SearchHistoryInteractor {
    override fun getTracksList(): ArrayList<Track> = searchHistoryRepository.getSearchHistoryList()

    override fun setTrackList(list: ArrayList<Track>) {
        searchHistoryRepository.setSearchHistoryList(list)
    }

    override fun clear() {
        searchHistoryRepository.clearHistory()
    }

    override fun saved(track: Track) {
        searchHistoryRepository.savedTrack(track)
    }

}