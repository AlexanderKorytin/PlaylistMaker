package com.example.playlistmaker.data.network

import android.content.SharedPreferences
import com.example.playlistmaker.domain.api.SearchHistory
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentetion.getTrackListFromJson
import com.example.playlistmaker.presentetion.ui.find.SEARCH_HISTORY_TRACK_LIST
import com.google.gson.Gson

class SearchHistoryImpl(val sharedPreference: SharedPreferences) : SearchHistory {

    internal var searchHistoryList = ArrayList<Track>()

    override fun savedTrack(track: Track) {
        val jsonTrackList = sharedPreference.getString(SEARCH_HISTORY_TRACK_LIST, null)
        if (jsonTrackList != null) {
            val historyList = getTrackListFromJson(jsonTrackList)
            searchHistoryList = savedTrackList(historyList, track)
        } else {
            searchHistoryList.add(0, track)
        }
        sharedPreference.edit()
            .putString(SEARCH_HISTORY_TRACK_LIST, createJsonFromTrackList(searchHistoryList))
            .apply()
    }

    override fun clearHistory() {
        searchHistoryList.clear()
        sharedPreference.edit().clear().apply()
    }

    override fun createJsonFromTrackList(tracks: ArrayList<Track>): String {
        return Gson().toJson(tracks)
    }

    override fun savedTrackList(tracks: ArrayList<Track>, track: Track): ArrayList<Track> {
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