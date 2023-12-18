package com.example.playlistmaker.currentplaylist.ui.models

import com.example.playlistmaker.search.domain.models.Track

sealed interface CurrentPlayListScreenState {
    data class Empty(val time: String): CurrentPlayListScreenState

    data class Content(val data: List<Track>, val time: String): CurrentPlayListScreenState
}