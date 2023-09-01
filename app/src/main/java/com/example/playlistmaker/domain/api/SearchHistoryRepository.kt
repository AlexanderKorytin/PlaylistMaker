package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryRepository {


    fun getSearchHistoryList(): ArrayList<Track>

    fun setSearchHistoryList(list: ArrayList<Track>)

    fun savedTrack(track: Track)

    fun clearHistory()

    fun createJsonFromTrackList(tracks: ArrayList<Track>): String

    fun savedTrackList(tracks: ArrayList<Track>, track: Track): ArrayList<Track>
}