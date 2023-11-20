package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.data.network.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.network.TracksRepositoryImpl
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.api.SearchTracksInteractor
import com.example.playlistmaker.search.domain.api.SetViewVisibilityUseCase
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.impl.ClearButtonSetViewVisibilityUseCase
import com.example.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.SearchTracksInteractorImpl
import org.koin.dsl.module

val searchDomainModule = module {

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(sharedPreferences = get(), json = get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(networkClient = get(), appDataBase = get())
    }

    factory<SetViewVisibilityUseCase> {
        ClearButtonSetViewVisibilityUseCase()
    }

    factory<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(searchHistoryRepository = get())
    }

    factory<SearchTracksInteractor> {
        SearchTracksInteractorImpl(trackRepository = get())
    }
}