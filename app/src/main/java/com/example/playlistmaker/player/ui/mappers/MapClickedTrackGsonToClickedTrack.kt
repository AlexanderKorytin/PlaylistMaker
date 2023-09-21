package com.example.playlistmaker.player.ui.mappers

import com.example.playlistmaker.player.ui.models.ClickedTrack
import com.example.playlistmaker.player.ui.models.ClickedTrackGson
import com.google.gson.Gson

class MapClickedTrackGsonToClickedTrack(private val json: Gson) {
    fun map(track: ClickedTrackGson): ClickedTrack {
        val clickedtrack = json.fromJson(track.trackGson, ClickedTrack::class.java)
        return clickedtrack
    }
}

