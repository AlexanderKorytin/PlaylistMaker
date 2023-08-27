package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.GetClickedTrackFromDataUseCase
import com.example.playlistmaker.domain.models.ClickedTrack
import com.example.playlistmaker.domain.models.ClickedTrackGson
import com.google.gson.Gson

class GetClickedTrackFromGsonUseCase(): GetClickedTrackFromDataUseCase {
    override fun execute(track: ClickedTrackGson): ClickedTrack {
            val clickedtrack =  Gson().fromJson(track.trackGson, ClickedTrack::class.java)
            return clickedtrack
    }
}

