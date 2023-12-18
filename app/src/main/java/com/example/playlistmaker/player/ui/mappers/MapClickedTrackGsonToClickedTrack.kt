package com.example.playlistmaker.player.ui.mappers

import com.example.playlistmaker.player.domain.models.ClickedTrack
import com.example.playlistmaker.player.ui.models.ClickedTrackGson
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MapClickedTrackGsonToClickedTrack(private val json: Gson) {
    fun map(track: ClickedTrackGson): ClickedTrack {
        val clickedtrack = json.fromJson(track.trackGson, ClickedTrack::class.java)
        return clickedtrack
    }
}

