package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentetion.getTrackList
import com.example.playlistmaker.ui.find.SEARCH_HISTORY_TRACK_LIST
import com.google.gson.Gson

class SearchHistory(val sharedPreference: SharedPreferences) {


    var searchHistoryList = ArrayList<Track>()

    fun savedTrack(track: Track) {
        val jsonTrackList = sharedPreference.getString(SEARCH_HISTORY_TRACK_LIST, null)
        if (jsonTrackList != null) {
            val historyList = getTrackList(jsonTrackList)
            searchHistoryList = savedTrackList(historyList, track)
        } else {
            searchHistoryList.add(0, track)
        }
        sharedPreference.edit()
            .putString(SEARCH_HISTORY_TRACK_LIST, createJsonFromTrackList(searchHistoryList))
            .apply()
    }

    fun clearHistory() {
        searchHistoryList.clear()
        sharedPreference.edit().clear().apply()
    }

    private fun createJsonFromTrackList(tracks: ArrayList<Track>): String {
        return Gson().toJson(tracks)
    }

    private fun savedTrackList(tracks: ArrayList<Track>, track: Track): ArrayList<Track> {
        tracks.forEach { t ->
            if (t.trackId == track.trackId) {
                tracks.remove(t)
                tracks.add(0, track)
                return tracks
            }
        }
        if (tracks.size < 10) {
            tracks.add(0, track)
            return tracks
        } else {
            tracks.removeAt(9)
            tracks.add(0, track)
            return tracks
        }
    }

}