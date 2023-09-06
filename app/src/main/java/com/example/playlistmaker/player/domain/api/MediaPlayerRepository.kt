package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.player.domain.models.PlayerState


interface MediaPlayerRepository {

    val playerState: PlayerState

    fun play()

    fun pause()

    fun release()

    fun getTimerStart(): Long

    fun getCurrentPosition(): Int
}