package com.example.playlistmaker.favoritetracks.di

import com.example.playlistmaker.favoritetracks.ui.viewmodel.FavoriteTracksViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoriteTracksViewModelViewModelModule = module {

    viewModel<FavoriteTracksViewModel> {
        FavoriteTracksViewModel(
            favoriteTracksInteractor = get(),
            json = get(),
            mapToTrackUI = get(),
            trackToTrackUI = get()
        )
    }

}