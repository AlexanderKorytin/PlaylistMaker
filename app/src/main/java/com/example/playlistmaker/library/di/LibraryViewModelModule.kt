package com.example.playlistmaker.library.di

import com.example.playlistmaker.library.ui.viewmodels.AlbumsListViewModel
import com.example.playlistmaker.library.ui.viewmodels.FavoriteTracksViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val libraryViewModelModule = module {

    viewModel<FavoriteTracksViewModel> { FavoriteTracksViewModel() }

    viewModel<AlbumsListViewModel> { AlbumsListViewModel() }

}