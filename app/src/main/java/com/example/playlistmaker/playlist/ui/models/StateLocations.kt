package com.example.playlistmaker.playlist.ui.models

import com.example.playlistmaker.playlist.domain.models.PlayList

sealed interface StateLocations{
    class isLocation(val playList: PlayList) : StateLocations
    class notLocation(val playList: PlayList) : StateLocations
}