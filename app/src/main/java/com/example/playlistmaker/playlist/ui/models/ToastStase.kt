package com.example.playlistmaker.playlist.ui.models

import com.example.playlistmaker.playlist.domain.models.PlayList

sealed interface ToastStase {
    class isLocation(val playList: PlayList) : ToastStase
    class notLocation(val playList: PlayList) : ToastStase
    object None : ToastStase
}