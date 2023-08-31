package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.GetTracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.models.Track

class GetGetTracksInteractorImpl(private val repositiry: TracksRepository) : GetTracksInteractor {

    override fun getMusic(term: String, consumer: Consumer<List<Track>>) {
       repositiry.getMusic(term, consumer)
    }

}