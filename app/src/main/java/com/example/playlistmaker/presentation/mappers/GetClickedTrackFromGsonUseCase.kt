package com.example.playlistmaker.presentation.mappers

import com.example.playlistmaker.presentation.models.ClickedTrack
import com.example.playlistmaker.presentation.models.ClickedTrackGson
import com.google.gson.Gson

class GetClickedTrackFromGsonUseCase() {
    fun map(track: ClickedTrackGson): ClickedTrack {
            val clickedtrack =  Gson().fromJson(track.trackGson, ClickedTrack::class.java)
            return clickedtrack
    }
}

