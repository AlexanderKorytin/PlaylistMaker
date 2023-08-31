package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.data.dto.PlayerState
import com.example.playlistmaker.data.mediaplayer.impl.MediaPlayerDataImpl
import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.models.ClickedTrack

class MediaPlayerInteractorImpl(track: ClickedTrack): MediaPlayerInteractor {
    private val mediaPlayerData = MediaPlayerDataImpl(track.toMediaPlayer())
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
    override fun getPlayerState(): PlayerState{
        return mediaPlayerData.playerState
    }
}