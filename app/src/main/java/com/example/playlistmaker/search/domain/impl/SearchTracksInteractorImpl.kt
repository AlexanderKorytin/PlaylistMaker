package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.SearchTracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.consumer.Consumer
import com.example.playlistmaker.search.domain.models.Track

class SearchTracksInteractorImpl(private val trackRepositiry: TracksRepository) :
    SearchTracksInteractor {

    override fun getMusic(term: String, consumer: Consumer<List<Track>>) {
        trackRepositiry.getMusic(term, consumer)
    }

}