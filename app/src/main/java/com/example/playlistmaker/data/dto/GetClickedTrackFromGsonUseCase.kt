package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.api.GetClickedTrackFromDataUseCase
import com.example.playlistmaker.domain.models.ClickedTrack
import com.google.gson.Gson

class GetClickedTrackFromGsonUseCase(): GetClickedTrackFromDataUseCase {
    override fun execute(track: Any?): ClickedTrack {
            val clickedtrack =  Gson().fromJson((track as String?), ClickedTrack::class.java)
            return clickedtrack
    }
}

