package com.example.playlistmaker.player.ui.models

import com.example.playlistmaker.player.domain.models.PlayerState

data class MediaPlayerScreenState(val currentTime: String, val playerState: PlayerState)

