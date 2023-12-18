package com.example.playlistmaker.currentplaylist.di

import com.example.playlistmaker.currentplaylist.data.db.repository.CurrentPlaListRepositoryImpl
import com.example.playlistmaker.currentplaylist.domain.api.CurrentPlayListInteractor
import com.example.playlistmaker.currentplaylist.domain.api.CurrentPlayListRepository
import com.example.playlistmaker.currentplaylist.domain.impl.CurrentPlayListInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val currentPlayListDomainModule = module {

    single<CurrentPlayListRepository> {
        CurrentPlaListRepositoryImpl(
            appDataBase = get(),
            currentPlayListTrackDBConverter = get(),
            json = get(),
            context = androidContext()
        )
    }

    factory<CurrentPlayListInteractor> {
        CurrentPlayListInteractorImpl(currentPlayListRepository = get())
    }
}