package com.example.playlistmaker.domain.api

interface MediaPlayerData {

    fun play()

    fun pause()

    fun release()

    fun getTimerStart(): Long

    fun getCurrentPosition(): Int
}