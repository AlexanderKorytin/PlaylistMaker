package com.example.playlistmaker.search.ui.viewmodel

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.ui.mappers.MapToTrackUI
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.SearchTracksInteractor
import com.example.playlistmaker.search.domain.api.SetViewVisibilityUseCase
import com.example.playlistmaker.search.domain.consumer.Consumer
import com.example.playlistmaker.search.domain.consumer.ConsumerData
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.models.SearchScreenState

class SearchViewModel(
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val trackInteractor: SearchTracksInteractor,
    private val setVisibilityClearButton: SetViewVisibilityUseCase
) : ViewModel() {
    companion object {
        private val SEARCH_REQUEST_TOKEN = Any()
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val handlerMain: Handler = Handler(Looper.getMainLooper())
    private val currentSearchViewScreenState = MutableLiveData<SearchScreenState>()
    fun getcurrentSearchViewScreenState(): LiveData<SearchScreenState> =
        currentSearchViewScreenState

    fun clearSearchHistory() {
        searchHistoryInteractor.clear()
        currentSearchViewScreenState.value = (SearchScreenState.EmptyHistory)
    }

    fun showSearchHistory(flag: Boolean) {
        val currentList = searchHistoryInteractor.getTracksList()
        if (flag && currentList.isNotEmpty()) {
            currentSearchViewScreenState.postValue(
                SearchScreenState.Hictory(
                    currentList.map { it -> it.toTrackUI() })
            )
        } else {
            currentSearchViewScreenState.value = (SearchScreenState.EmptyHistory)
        }

    }

    private var latestSearchText: String? = null

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        this.latestSearchText = changedText
        handlerMain.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { getMusic(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handlerMain.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    fun getMusic(text: String) {
        if (text.isNotEmpty()) {
            currentSearchViewScreenState.value = SearchScreenState.isLoading
            val result = trackInteractor.getMusic(text, object : Consumer<List<Track>> {
                override fun consume(data: ConsumerData<List<Track>>) {
                    when (data) {
                        is ConsumerData.Data -> {
                            if (data.value.isNotEmpty()) {
                                currentSearchViewScreenState.postValue(
                                    SearchScreenState.Content(
                                        MapToTrackUI().mapList(data.value)
                                    )
                                )
                            } else {
                                currentSearchViewScreenState.postValue(SearchScreenState.Empty)
                            }
                        }

                        is ConsumerData.Error -> {
                            currentSearchViewScreenState.postValue(SearchScreenState.Error(""))
                        }

                    }
                }

            })
        }
    }

    override fun onCleared() {
        handlerMain.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

}