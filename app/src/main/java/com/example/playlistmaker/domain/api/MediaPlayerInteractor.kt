package com.example.playlistmaker.domain.api

import com.example.playlistmaker.data.dto.PlayerState
import com.example.playlistmaker.domain.models.ClickedTrack

interface MediaPlayerInteractor {
    fun play()

    fun pause()

    fun release()

    fun getTimerStart(): Long

    fun getCurrentPosition(): Int

    fun getPlayerState(): PlayerState
}