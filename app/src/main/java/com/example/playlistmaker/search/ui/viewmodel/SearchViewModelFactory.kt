package com.example.playlistmaker.search.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.Util.App

class SearchViewModelFactory(private val context: Context): ViewModelProvider.Factory {

    private val searchHistoryInteractor by lazy { App().creator.provideGetSearchHistoryInteractor(context) }
    private val trackInteractor = App().creator.provideGetTrackInteractor(context)
    private val setVisibilityClearButton = App().creator.provideGetSetViewVisibilityUseCase()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(searchHistoryInteractor, trackInteractor, setVisibilityClearButton) as T
    }
}