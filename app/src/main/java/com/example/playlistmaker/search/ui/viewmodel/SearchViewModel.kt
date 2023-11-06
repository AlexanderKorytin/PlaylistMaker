package com.example.playlistmaker.search.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.app.debounce
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.SearchTracksInteractor
import com.example.playlistmaker.search.domain.api.SetViewVisibilityUseCase
import com.example.playlistmaker.search.ui.mappers.MapToTrackUI
import com.example.playlistmaker.search.ui.mappers.TrackToTrackUI
import com.example.playlistmaker.search.ui.models.SearchScreenState
import com.example.playlistmaker.search.ui.models.TrackUI
import com.google.gson.Gson
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val trackInteractor: SearchTracksInteractor,
    private val setVisibilityClearButton: SetViewVisibilityUseCase,
    private val trackToTrackUI: TrackToTrackUI,
    val mapToTrackUI: MapToTrackUI,
    val json: Gson
) : ViewModel() {

    private val searchTrackDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) {
            getMusic(it)
        }
    private val currentSearchViewScreenState = MutableLiveData<SearchScreenState>()
    fun getcurrentSearchViewScreenState(): LiveData<SearchScreenState> =
        currentSearchViewScreenState

    fun clearSearchHistory() {
        searchHistoryInteractor.clear()
        currentSearchViewScreenState.value = (SearchScreenState.EmptyHistory(emptyList(), false))
    }

    fun showSearchHistory(flag: Boolean) {
        val currentList = searchHistoryInteractor.getTracksList()
        if (flag && currentList.isNotEmpty()) {
            currentSearchViewScreenState.postValue(
                SearchScreenState.Hictory(
                    currentList.map { it -> trackToTrackUI.fromTrack(it) }, false
                )
            )
        } else {
            currentSearchViewScreenState.value =
                (SearchScreenState.EmptyHistory(emptyList(), false))
        }
    }

    private var latestSearchText: String? = null

    fun searchDebounce(textSearch: String) {
        if (latestSearchText != textSearch) {
            latestSearchText = textSearch
            searchTrackDebounce(textSearch)
        }
    }

    fun getSearchHstoryTracks() = searchHistoryInteractor.getTracksList()
    fun savedTrack(track: TrackUI) {
        searchHistoryInteractor.saved(track.toTrack())
    }

    fun getMusic(textSearch: String) {
        if (textSearch.isNotEmpty()) {
            currentSearchViewScreenState.value =
                SearchScreenState.IsLoading(setVisibilityClearButton.execute(textSearch))

            viewModelScope.launch {
                trackInteractor.getMusic(textSearch)
                    .collect { pair ->
                        processResult(mapToTrackUI.mapList(pair.first), pair.second)
                    }
            }
        }
    }

    fun stop() {
        onCleared()
    }

    private fun processResult(data: List<TrackUI>?, error: String?) {
        if (data != null) {
            if (data.isNotEmpty()) {
                currentSearchViewScreenState.postValue(
                    SearchScreenState.Content(
                        data,
                        setVisibilityClearButton.execute(error)
                    )
                )
            } else {
                currentSearchViewScreenState.postValue(
                    SearchScreenState.Empty(
                        setVisibilityClearButton.execute(error)
                    )
                )
            }
        }

        if (error != null) {
            currentSearchViewScreenState.postValue(
                SearchScreenState.Error(
                    "",
                    setVisibilityClearButton.execute(error)
                )
            )
        }

    }


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}