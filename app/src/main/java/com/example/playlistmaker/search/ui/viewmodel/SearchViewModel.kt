package com.example.playlistmaker.search.ui.viewmodel

import android.os.Handler
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.app.debounce
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.SearchTracksInteractor
import com.example.playlistmaker.search.domain.api.SetViewVisibilityUseCase
import com.example.playlistmaker.search.domain.consumer.Consumer
import com.example.playlistmaker.search.domain.consumer.ConsumerData
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.mappers.MapToTrackUI
import com.example.playlistmaker.search.ui.mappers.TrackToTrackUI
import com.example.playlistmaker.search.ui.models.SearchScreenState
import com.google.gson.Gson

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
    fun savedTrack(track: Track) {
        searchHistoryInteractor.saved(track)
    }

    fun getMusic(textSearch: String) {
        if (textSearch.isNotEmpty()) {
            currentSearchViewScreenState.value =
                SearchScreenState.IsLoading(setVisibilityClearButton.execute(textSearch))
            trackInteractor.getMusic(textSearch, object : Consumer<List<Track>> {
                override fun consume(data: ConsumerData<List<Track>>) {
                    when (data) {
                        is ConsumerData.Data -> {
                            if (data.value.isNotEmpty()) {
                                currentSearchViewScreenState.postValue(
                                    SearchScreenState.Content(
                                        mapToTrackUI.mapList(data.value),
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

                        is ConsumerData.Error -> {
                            currentSearchViewScreenState.postValue(
                                SearchScreenState.Error(
                                    "",
                                    setVisibilityClearButton.execute(textSearch)
                                )
                            )
                        }

                    }
                }

            })
        }
    }

    fun stop() {
        onCleared()
    }

    companion object {
        private val SEARCH_REQUEST_TOKEN = Any()
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}