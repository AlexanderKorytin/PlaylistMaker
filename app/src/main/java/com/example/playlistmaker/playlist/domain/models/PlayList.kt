package com.example.playlistmaker.playlist.domain.models

import com.google.gson.Gson
import java.io.File

data class PlayList(
    val playListId: Int = 0,
    var playListName: String,
    var playListDescription: String,
    var playListCover: String,
    var tracksIds: ArrayList<Long>,
    var quantityTracks: Int = 0
)
