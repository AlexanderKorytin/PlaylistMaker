package com.example.playlistmaker.playlists.di

import com.example.playlistmaker.playlist.ui.viewmodel.EditPlaylistViewModel
import com.example.playlistmaker.playlist.ui.viewmodel.PlayListsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playListsViewModelModule = module {
    viewModel { PlayListsViewModel(interactor = get(), json = get()) }
}

val editPlayListViewModelModule = module {
    viewModel<EditPlaylistViewModel> { (playlistId: Int) ->
        EditPlaylistViewModel(
            playlistId,
            playListInteractor = get(),
            currentPlayListInteractor = get(),
            json = get()
        )
    }
}