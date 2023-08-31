package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.PlayerState


interface MediaPlayerData {

    val playerState: PlayerState

    fun play()

    fun pause()

    fun release()

    fun getTimerStart(): Long

    fun getCurrentPosition(): Int
}