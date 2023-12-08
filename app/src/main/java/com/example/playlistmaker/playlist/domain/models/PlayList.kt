package com.example.playlistmaker.playlist.domain.models

import java.io.File

data class PlayList(
    val playListId: Int = 0,
    val playListName: String,
    val playListDescription: String,
    val playListCover: String,
    val tracksIds: String = "",
    val quantityTracks: Int = 0
)
