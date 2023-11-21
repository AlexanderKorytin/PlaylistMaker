package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.player.domain.models.ClickedTrack
import com.example.playlistmaker.player.domain.models.PlayerState

interface MediaPlayerInteractor {
    fun prepare(travUI: ClickedTrack)
    fun play()

    fun pause()

    fun release()

    fun getTimerStart(): Long

    fun getCurrentPosition(): Int

    fun getPlayerState(): PlayerState

    suspend fun getSingFavoriteTrack(clickedTrack: ClickedTrack): Boolean
}