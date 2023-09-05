package com.example.playlistmaker.presentation.ui.mediaplayer.models

import com.example.playlistmaker.domain.models.PlayerState
import com.example.playlistmaker.presentation.models.ClickedTrack

sealed class MediaPlayerScreenState {
    object Loading : MediaPlayerScreenState()

    data class Content(
        val clickedTrack: ClickedTrack?,
        val currentTime: String?,
        val playerState: PlayerState?
    ) : MediaPlayerScreenState()
}

