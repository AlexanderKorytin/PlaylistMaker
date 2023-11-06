package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.SearchResultData
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun getMusic(term: String): Flow<SearchResultData<List<Track>>>
}