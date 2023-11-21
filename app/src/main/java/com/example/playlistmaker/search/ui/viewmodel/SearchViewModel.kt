package com.example.playlistmaker.search.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.SearchTracksInteractor
import com.example.playlistmaker.search.domain.api.SetViewVisibilityUseCase
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.mappers.MapToTrackUI
import com.example.playlistmaker.search.ui.mappers.TrackToTrackUI
import com.example.playlistmaker.search.ui.models.SearchScreenState
import com.example.playlistmaker.util.debounce
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                getSearchHstoryTracks().collect { listFavorite ->
                    if (flag && listFavorite.isNotEmpty()) {
                        currentSearchViewScreenState.postValue(
                            SearchScreenState.Hictory(
                                listFavorite.map { it -> trackToTrackUI.map(it) }, false
                            )
                        )
                    } else {
                        currentSearchViewScreenState.postValue(
                            (SearchScreenState.EmptyHistory(emptyList(), false))
                        )
                    }
                }

            }
        }
    }

    private var latestSearchText: String? = null
    fun searchDebounce(textSearch: String) {
        currentSearchViewScreenState.value =
            SearchScreenState.Start(setVisibilityClearButton.execute(textSearch))
        if (latestSearchText != textSearch) {
            latestSearchText = textSearch
            searchTrackDebounce(textSearch)
        }
    }

    private fun getSearchHstoryTracks(): Flow<ArrayList<Track>> {
        return searchHistoryInteractor.getTracksList()
    }

    fun savedTrack(track: Track) {
        searchHistoryInteractor.saved(track)
    }

    fun getMusic(textSearch: String) {
        if (textSearch.isNotEmpty()) {
            currentSearchViewScreenState.value =
                SearchScreenState.IsLoading(setVisibilityClearButton.execute(textSearch))

            viewModelScope.launch {
                trackInteractor.getMusic(textSearch)
                    .collect { result ->
                        processResult(result.tracks, result.errorMessage, textSearch)
                    }
            }
        }
    }

    fun stop() {
        onCleared()
    }

    private fun processResult(data: List<Track>?, error: String?, textSearch: String) {
        if (data != null) {
            if (data.isNotEmpty()) {
                currentSearchViewScreenState.postValue(
                    SearchScreenState.Content(
                        mapToTrackUI.mapList(data),
                        setVisibilityClearButton.execute(textSearch)
                    )
                )
            } else {
                currentSearchViewScreenState.postValue(
                    SearchScreenState.Empty(
                        setVisibilityClearButton.execute(textSearch)
                    )
                )
            }
        }

        if (error != null) {
            currentSearchViewScreenState.postValue(
                SearchScreenState.Error(
                    "",
                    setVisibilityClearButton.execute(textSearch)
                )
            )
        }

    }


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}