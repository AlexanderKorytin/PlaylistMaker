package com.example.playlistmaker.playlists.di

import com.example.playlistmaker.playlist.ui.viewmodel.PlayListsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playListsViewModelModule = module {

    viewModel<PlayListsViewModel> { PlayListsViewModel(interactor = get(), json = get()) }

}