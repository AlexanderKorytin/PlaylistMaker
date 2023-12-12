package com.example.playlistmaker.playlist.domain.models

import com.google.gson.Gson
import java.io.File

data class PlayList(
    val playListId: Int = 0,
    val playListName: String,
    val playListDescription: String,
    val playListCover: String,
    var tracksIds: String,
    var quantityTracks: Int = 0
)
