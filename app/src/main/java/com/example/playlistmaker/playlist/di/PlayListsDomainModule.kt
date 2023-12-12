package com.example.playlistmaker.playlist.di

import com.example.playlistmaker.playlist.data.db.repository.PlayListRepositoryImpl
import com.example.playlistmaker.playlist.domain.api.PlayListInteractor
import com.example.playlistmaker.playlist.domain.api.PlayListsRepository
import com.example.playlistmaker.playlist.domain.impl.PlayListInteractorImpl
import org.koin.dsl.module

val playListsDomainModule = module {
    single<PlayListsRepository> {
        PlayListRepositoryImpl(appDataBase = get(), converter = get())
    }

    factory<PlayListInteractor> {
        PlayListInteractorImpl(repository = get())
    }
}