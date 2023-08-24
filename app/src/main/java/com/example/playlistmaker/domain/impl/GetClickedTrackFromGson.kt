package com.example.playlistmaker.domain.impl

import android.content.Intent
import com.example.playlistmaker.domain.api.GetClickedTrackFromData
import com.example.playlistmaker.domain.models.ClickedTrack
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class GetClickedTrackFromGson(): GetClickedTrackFromData {
    override fun execute(track: Any?): ClickedTrack {
            val clickedtrack =  Gson().fromJson((track as String?), ClickedTrack::class.java)
            return clickedtrack
    }
}

