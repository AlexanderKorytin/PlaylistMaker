package com.example.playlistmaker.domain.impl

import android.content.SharedPreferences
import com.example.playlistmaker.data.network.SearchHistoryImpl
import com.example.playlistmaker.domain.api.SearchHistory
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.models.Track

class SearchHistoryInteractorImpl(val searchHistory: SearchHistory): SearchHistoryInteractor {
    override fun getTracksList(): ArrayList<Track> = searchHistory.searchHistoryList

    override fun setTrackList(list: ArrayList<Track>) {
        searchHistory.searchHistoryList = list
    }

    override fun clear() {
        searchHistory.clearHistory()
    }

    override fun saved(track: Track) {
        searchHistory.savedTrack(track)
    }

}