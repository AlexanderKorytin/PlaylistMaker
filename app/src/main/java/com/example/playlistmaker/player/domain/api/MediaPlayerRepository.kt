package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.player.domain.models.ClickedTrack
import com.example.playlistmaker.player.domain.models.PlayerState


interface MediaPlayerRepository {


    fun preparePlayer(clickedTrack: ClickedTrack)

    fun play()

    fun pause()

    fun release()

    fun getTimerStart(): Long

    fun getCurrentPosition(): Int
    fun gerMediaPlayerState(): PlayerState
}