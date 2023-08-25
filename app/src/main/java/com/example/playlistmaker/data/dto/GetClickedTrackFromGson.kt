package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.api.GetClickedTrackFromData
import com.example.playlistmaker.domain.models.ClickedTrack
import com.google.gson.Gson

class GetClickedTrackFromGson(): GetClickedTrackFromData {
    override fun execute(track: Any?): ClickedTrack {
            val clickedtrack =  Gson().fromJson((track as String?), ClickedTrack::class.java)
            return clickedtrack
    }
}

