package com.example.playlistmaker.search.ui.models

sealed class SearchScreenState {
    object isLoading : SearchScreenState()

    data class Content(val tracks: List<TrackUI>): SearchScreenState()

    data class Error(val message: String): SearchScreenState()

    data class Empty(val message: String): SearchScreenState()

}