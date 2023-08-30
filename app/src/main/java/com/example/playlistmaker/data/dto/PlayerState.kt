package com.example.playlistmaker.data.dto

enum class PlayerState(val i: Int) {

    STATE_DEFAULT(0),
    STATE_PREPARED(1),
    STATE_PLAYING(2),
    STATE_PAUSED(3)
}