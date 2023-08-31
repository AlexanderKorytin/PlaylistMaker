package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface GetTracksInteractor {
    fun getMusic(expression: String): List<Track>?

}