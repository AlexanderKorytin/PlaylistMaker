package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryInteractor {

    fun getTracksList(): Flow<ArrayList<Track>>
    fun clear()
    fun saved(track: Track)
}