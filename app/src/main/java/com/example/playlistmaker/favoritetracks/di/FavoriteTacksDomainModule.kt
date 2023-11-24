package com.example.playlistmaker.favoritetracks.di

import com.example.playlistmaker.favorite.data.db.repository.FavoriteTracksRepositoryImpl
import com.example.playlistmaker.favorite.domain.api.FavoriteTracksDatabaseRepository
import com.example.playlistmaker.favorite.domain.api.FavoriteTracksInteractor
import com.example.playlistmaker.favorite.domain.impl.FavoriteTracksInteractorImpl
import org.koin.dsl.module

val favoriteTracksDomainModule = module {

    single<FavoriteTracksDatabaseRepository> {
        FavoriteTracksRepositoryImpl(appDatabase = get(), converter = get())
    }

    factory<FavoriteTracksInteractor> {
        FavoriteTracksInteractorImpl(favoriteTracksDatabaseRepository = get())
    }
}