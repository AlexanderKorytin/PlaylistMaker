package com.example.playlistmaker.search.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.api.GetTracksInteractor
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.SetViewVisibilityUseCase

class SearchViewModel(
    private val searchHistoryInteractorI: SearchHistoryInteractor,
    private val trackInteractor: GetTracksInteractor,
    private val setVisibilityClearButton: SetViewVisibilityUseCase
) : ViewModel() {

}