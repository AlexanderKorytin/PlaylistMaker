package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.consumer.Consumer
import com.example.playlistmaker.search.domain.models.Track

interface SearchTracksInteractor {
    fun getMusic(term: String, consumer: Consumer<List<Track>>)

}