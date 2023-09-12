package com.example.playlistmaker.search.ui.models

sealed class SearchScreenState {
    data class Start(val isVisibility: Boolean) : SearchScreenState()
    data class IsLoading(val isVisibility: Boolean) : SearchScreenState()

    data class Content(val tracks: List<TrackUI>, val isVisibility: Boolean) : SearchScreenState()

    data class Error(val message: String, val isVisibility: Boolean) : SearchScreenState()

    data class Empty(val isVisibility: Boolean) : SearchScreenState()

    data class EmptyHistory(val tracks: List<TrackUI>, val isVisibility: Boolean) :
        SearchScreenState()

    data class Hictory(val tracks: List<TrackUI>, val isVisibility: Boolean) : SearchScreenState()

}