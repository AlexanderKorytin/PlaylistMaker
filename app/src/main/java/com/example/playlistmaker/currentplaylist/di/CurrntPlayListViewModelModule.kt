package com.example.playlistmaker.currentplaylist.di

import com.example.playlistmaker.currentplaylist.ui.viewmodel.CurrentPlayListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val currentPlayListViewModelModule = module {
    viewModel { (playlist: String?) ->
        CurrentPlayListViewModel(
            playList = playlist,
            currentPlayListInteractor = get(),
            json = get(),
            mapper = get()
        )
    }
}