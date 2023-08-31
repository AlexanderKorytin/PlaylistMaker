package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.MediaPlayerData
import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.models.PlayerState


class MediaPlayerInteractorImpl( private val mediaPlayerData: MediaPlayerData): MediaPlayerInteractor {
    override fun play(){
        mediaPlayerData.play()
    }

    override fun pause(){
        mediaPlayerData.pause()
    }

    override fun release(){
        mediaPlayerData.release()
    }

    override fun getTimerStart(): Long {
        return mediaPlayerData.getTimerStart()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayerData.getCurrentPosition()
    }
    override fun getPlayerState(): PlayerState {
        return mediaPlayerData.playerState
    }
}