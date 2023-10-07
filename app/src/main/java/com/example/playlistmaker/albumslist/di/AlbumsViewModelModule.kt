package com.example.playlistmaker.albumslist.di

import com.example.playlistmaker.albumslist.ui.viewmodel.AlbumsListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val albumsViewModelModule = module {

    viewModel<AlbumsListViewModel> { AlbumsListViewModel() }

}