package com.example.playlistmaker.playlist.ui.models

import com.example.playlistmaker.playlist.domain.models.PlayList

sealed interface PlayListsScreenState {
    data class PlayListsContent(val data: List<PlayList>) : PlayListsScreenState
    object Empty : PlayListsScreenState
}