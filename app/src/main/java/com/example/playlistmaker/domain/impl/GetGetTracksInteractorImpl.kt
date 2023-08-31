package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.GetTracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.Track

class GetGetTracksInteractorImpl(private val repositiry: TracksRepository) : GetTracksInteractor {

    override fun getMusic(term: String): List<Track>? {
       return repositiry.getMusic(term)
    }

}