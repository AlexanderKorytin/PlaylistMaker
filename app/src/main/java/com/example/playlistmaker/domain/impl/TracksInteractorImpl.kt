package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.Track

class TracksInteractorImpl(private val repositiry: TracksRepository) : TracksInteractor {

    override fun getMusic(term: String): List<Track>? {
       return repositiry.getMusic(term)
    }

}