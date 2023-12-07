package com.example.playlistmaker.search.data.network

import android.content.SharedPreferences
import com.example.playlistmaker.app.getTrackListFromJson
import com.example.playlistmaker.core.db.AppDataBase
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

const val SEARCH_HISTORY_TRACK_LIST = "Search history list"

class SearchHistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val json: Gson,
    private val appDataBase: AppDataBase,
) :
    SearchHistoryRepository {

    private var searchHistoryList = ArrayList<Track>()

    override fun getSearchHistoryList(): Flow<ArrayList<Track>> = flow {
        val listId = appDataBase.getFavoriteTrackDao().getIdFavoriteTracks()
        searchHistoryList = getTrackListFromJson(
            sharedPreferences.getString(
                SEARCH_HISTORY_TRACK_LIST,
                null
            )
        )
        searchHistoryList.forEach {
            it.inFavorite = listId.contains(it.trackId)
        }
        emit(searchHistoryList)
    }

    override fun savedTrack(track: Track) {
        val jsonTrackList = sharedPreferences.getString(SEARCH_HISTORY_TRACK_LIST, null)
        if (jsonTrackList != null) {
            val historyList = getTrackListFromJson(jsonTrackList)
            searchHistoryList = savedTrackList(historyList, track)
        } else {
            searchHistoryList.add(0, track)
        }
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_TRACK_LIST, createJsonFromTrackList(searchHistoryList))
            .apply()
    }

    override fun clearHistory() {
        searchHistoryList.clear()
        sharedPreferences.edit().clear().apply()
    }

    override fun createJsonFromTrackList(tracks: ArrayList<Track>): String {
        return json.toJson(tracks)
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