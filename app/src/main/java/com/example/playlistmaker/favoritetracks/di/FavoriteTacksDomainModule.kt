package com.example.playlistmaker.favoritetracks.di

import com.example.playlistmaker.favoritetracks.db.repository.FavoriteTracksRepositoryImpl
import com.example.playlistmaker.favoritetracks.domain.api.FavoriteTracksDatabaseRepository
import com.example.playlistmaker.favoritetracks.domain.api.FavoriteTracksInteractor
import com.example.playlistmaker.favoritetracks.domain.impl.FavoriteTracksInteractorImpl
import org.koin.dsl.module

val favoriteTracksDomainModule = module {

    single<FavoriteTracksDatabaseRepository> {
        FavoriteTracksRepositoryImpl(appDatabase = get(), converter = get())
    }

    factory<FavoriteTracksInteractor> {
        FavoriteTracksInteractorImpl(favoriteTracksDatabaseRepository = get())
    }
}