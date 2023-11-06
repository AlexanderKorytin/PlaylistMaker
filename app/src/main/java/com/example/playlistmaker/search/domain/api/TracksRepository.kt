package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.consumer.Consumer
import com.example.playlistmaker.search.domain.consumer.ConsumerData
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun getMusic(term: String): Flow<ConsumerData<List<Track>>>
}