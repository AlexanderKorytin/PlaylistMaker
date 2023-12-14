package com.example.playlistmaker.currentplaylist.ui.models

import com.example.playlistmaker.search.domain.models.Track

sealed interface CurrentPlayListScreenState {
    data class Empty(val playListName: String): CurrentPlayListScreenState

    data class Content(val playListName: String, val data: List<Track>): CurrentPlayListScreenState
}