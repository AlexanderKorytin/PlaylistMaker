package com.example.playlistmaker.domain.impl

import android.content.SharedPreferences
import com.example.playlistmaker.data.network.SearchHistoryImpl
import com.example.playlistmaker.domain.models.Track

class SearchHistoryUseCase(sharedPreferences: SharedPreferences) {

    private val searchHistory = SearchHistoryImpl(sharedPreferences)

    fun getTracksList(): ArrayList<Track> = searchHistory.searchHistoryList

    fun setTrackList(list: ArrayList<Track>) {
        searchHistory.searchHistoryList = list
    }

    fun clear() {
        searchHistory.clearHistory()
    }

    fun saved(track: Track) {
        searchHistory.savedTrack(track)
    }

}