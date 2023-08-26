package com.example.playlistmaker.data

import com.example.playlistmaker.domain.models.Track

interface SearchHistory {
    fun savedTrack(track: Track)

    fun clearHistory()

    fun createJsonFromTrackList(tracks: ArrayList<Track>): String

    fun savedTrackList(tracks: ArrayList<Track>, track: Track): ArrayList<Track>
}