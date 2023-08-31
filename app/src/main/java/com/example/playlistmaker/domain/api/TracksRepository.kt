package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.models.Track

interface TracksRepository {
    fun getMusic(term: String, consumer: Consumer<List<Track>>)


}