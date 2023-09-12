package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track

interface SearchHistoryInteractor {

    fun getTracksList(): ArrayList<Track>
    fun clear()
    fun saved(track: Track)
}