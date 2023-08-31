package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface SearchHistory {

    var searchHistoryList: ArrayList<Track>

    fun savedTrack(track: Track)

    fun clearHistory()

    fun createJsonFromTrackList(tracks: ArrayList<Track>): String

    fun savedTrackList(tracks: ArrayList<Track>, track: Track): ArrayList<Track>
}