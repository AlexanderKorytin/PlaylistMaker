package com.example.playlistmaker.data.mediaplayer.api

interface MediaPlayerData {

    fun play()

    fun pause()

    fun release()

    fun getTimerStart(): Long

    fun getCurrentPosition(): Int
}