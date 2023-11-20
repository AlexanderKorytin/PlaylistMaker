package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {


    fun getSearchHistoryList(): Flow<ArrayList<Track>>

    fun savedTrack(track: Track)

    fun clearHistory()

    fun createJsonFromTrackList(tracks: ArrayList<Track>): String

    fun savedTrackList(tracks: ArrayList<Track>, track: Track): ArrayList<Track>
}