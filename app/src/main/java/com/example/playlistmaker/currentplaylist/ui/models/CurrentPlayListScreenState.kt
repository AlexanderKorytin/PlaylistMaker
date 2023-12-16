package com.example.playlistmaker.currentplaylist.ui.models

import com.example.playlistmaker.search.domain.models.Track

sealed interface CurrentPlayListScreenState {
    object Empty: CurrentPlayListScreenState

    data class Content(val data: List<Track>, val time: String): CurrentPlayListScreenState
}