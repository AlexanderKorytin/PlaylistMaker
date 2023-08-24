package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.ClickedTrack

interface GetClickedTrackFromData {
    fun execute(track: Any?): ClickedTrack
}