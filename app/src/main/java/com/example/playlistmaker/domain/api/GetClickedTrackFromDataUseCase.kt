package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.ClickedTrack
import com.example.playlistmaker.domain.models.ClickedTrackGson

interface GetClickedTrackFromDataUseCase {
    fun execute(track: ClickedTrackGson): ClickedTrack
}