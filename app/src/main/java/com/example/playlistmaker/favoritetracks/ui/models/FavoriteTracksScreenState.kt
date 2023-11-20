package com.example.playlistmaker.favoritetracks.ui.models

import com.example.playlistmaker.search.domain.models.Track

sealed interface FavoriteTracksScreenState{

    data class FavoriteTracksContent(val data: List<Track>): FavoriteTracksScreenState

    class FavoriteTracksEmpty(): FavoriteTracksScreenState

}