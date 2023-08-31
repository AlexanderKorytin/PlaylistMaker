package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.models.Track

interface GetTracksInteractor {
    fun getMusic(expression: String, consumer: Consumer<List<Track>>)

}