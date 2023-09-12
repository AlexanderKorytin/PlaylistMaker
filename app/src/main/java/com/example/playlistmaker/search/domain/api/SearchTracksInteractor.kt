package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.consumer.Consumer
import com.example.playlistmaker.search.domain.models.Track

interface SearchTracksInteractor {
    fun getMusic(expression: String, consumer: Consumer<List<Track>>)

}