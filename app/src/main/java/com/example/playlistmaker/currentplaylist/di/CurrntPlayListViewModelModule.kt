package com.example.playlistmaker.currentplaylist.di

import com.example.playlistmaker.currentplaylist.ui.viewmodel.CurrentPlayListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val currentPlayListViewModelModule = module {
    viewModel { (playlistId: Int) ->
        CurrentPlayListViewModel(
            playListId = playlistId,
            currentPlayListInteractor = get(),
            mapper = get(),
            json = get()
        )
    }
}