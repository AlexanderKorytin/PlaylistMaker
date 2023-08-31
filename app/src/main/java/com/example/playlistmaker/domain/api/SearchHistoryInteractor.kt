package com.example.playlistmaker.domain.api

import com.example.playlistmaker.data.network.SearchHistoryImpl
import com.example.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {

    fun getTracksList(): ArrayList<Track>

    fun setTrackList(list: ArrayList<Track>)

    fun clear()

    fun saved(track: Track)
}