package com.example.playlistmaker.search.ui.models

sealed class SearchScreenState {
    val isVisibility = false
    object isLoading : SearchScreenState()

    data class Content(val tracks: List<TrackUI>): SearchScreenState()

    data class Error(val message: String): SearchScreenState()

    object Empty: SearchScreenState()

    object EmptyHistory: SearchScreenState()

    data class Hictory(val tracks: List<TrackUI>): SearchScreenState()

}