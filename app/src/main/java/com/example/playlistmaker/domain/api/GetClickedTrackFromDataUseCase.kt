package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.ClickedTrack

interface GetClickedTrackFromDataUseCase {
    fun execute(track: Any?): ClickedTrack
}