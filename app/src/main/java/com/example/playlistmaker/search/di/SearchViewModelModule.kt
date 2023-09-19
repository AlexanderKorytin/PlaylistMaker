package com.example.playlistmaker.search.di

import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.search.ui.mappers.MapToTrackUI
import com.example.playlistmaker.search.ui.mappers.TrackToTrackUI
import com.example.playlistmaker.search.ui.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val searchViewModelModule = module {

    single<Handler> {
        Handler(Looper.getMainLooper())
    }

    factory<TrackToTrackUI> {
        TrackToTrackUI
    }

    factory<MapToTrackUI> {
        MapToTrackUI()
    }

    viewModel<SearchViewModel> {
        SearchViewModel(
            searchHistoryInteractor = get(),
            trackInteractor = get(),
            setVisibilityClearButton = get(),
            handlerMain = get(),
            trackToTrackUI = get(),
            mapToTrackUI = get()
        )
    }

}