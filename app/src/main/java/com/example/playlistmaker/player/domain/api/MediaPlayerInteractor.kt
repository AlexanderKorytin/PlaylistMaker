package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.player.domain.models.PlayerState

interface MediaPlayerInteractor {
    fun play()

    fun pause()

    fun release()

    fun getTimerStart(): Long

    fun getCurrentPosition(): Int

    fun getPlayerState(): PlayerState
}