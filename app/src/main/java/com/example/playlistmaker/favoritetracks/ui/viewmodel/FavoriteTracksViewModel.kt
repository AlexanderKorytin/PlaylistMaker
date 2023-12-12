package com.example.playlistmaker.favoritetracks.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.favoritetracks.domain.api.FavoriteTracksInteractor
import com.example.playlistmaker.favoritetracks.ui.models.FavoriteTracksScreenState
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.mappers.MapToTrackUI
import com.example.playlistmaker.search.ui.mappers.TrackToTrackUI
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteTracksViewModel(
    private val favoriteTracksInteractor: FavoriteTracksInteractor,
    private val trackToTrackUI: TrackToTrackUI,
    val mapToTrackUI: MapToTrackUI,
    val json: Gson
) : ViewModel() {

    init {
        updateFavoriteTracks()
    }

    private val currentFavoriteTracksScreenState: MutableLiveData<FavoriteTracksScreenState> =
        MutableLiveData()

    fun getScreenState(): LiveData<FavoriteTracksScreenState> = currentFavoriteTracksScreenState

    fun updateFavoriteTracks() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                favoriteTracksInteractor.getFavoriteTracks().collect { tracks ->
                    progressResult(tracks)
                }
            }
        }
    }

    private fun progressResult(tracks: List<Track>) {
        when {
            tracks.isEmpty() -> currentFavoriteTracksScreenState.postValue(FavoriteTracksScreenState.FavoriteTracksEmpty())
            else -> currentFavoriteTracksScreenState.postValue(
                FavoriteTracksScreenState.FavoriteTracksContent(
                    tracks
                )
            )
        }
    }

}