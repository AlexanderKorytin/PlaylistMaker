package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.data.dto.PlayerState
import com.example.playlistmaker.data.dto.TrackUrl
import com.example.playlistmaker.data.mediaplayer.api.MediaPlayerData
import com.example.playlistmaker.data.mediaplayer.impl.MediaPlayerDataImpl
import com.example.playlistmaker.domain.models.ClickedTrack

class MediaPlayerInteractor(track: ClickedTrack) {
    private val mediaPlayerData = MediaPlayerDataImpl(track.toMediaPlayer())
    fun play(){
        mediaPlayerData.play()
    }

    fun pause(){
        mediaPlayerData.pause()
    }

    fun release(){
        mediaPlayerData.release()
    }

    fun getTimerStart(): Long {
        return mediaPlayerData.getTimerStart()
    }

    fun getCurrentPosition(): Int {
        return mediaPlayerData.getCurrentPosition()
    }
    fun getPlayerState(): PlayerState{
        return mediaPlayerData.playerState
    }
}