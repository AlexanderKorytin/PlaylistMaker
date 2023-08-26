package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository

class TracksInteractorImpl(private  val repositiry: TracksRepository): TracksInteractor {

    override fun getMusic(term: String) {
        repositiry.getMusic(term)
    }

}