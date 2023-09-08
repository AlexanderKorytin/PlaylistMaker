package com.example.playlistmaker.player.ui.mappers

import com.example.playlistmaker.player.ui.models.ClickedTrack
import com.example.playlistmaker.player.ui.models.ClickedTrackGson
import com.google.gson.Gson

class GetClickedTrackFromGsonUseCase {
    fun map(track: ClickedTrackGson): ClickedTrack {
        val clickedtrack = Gson().fromJson(track.trackGson, ClickedTrack::class.java)
        return clickedtrack
    }
}

