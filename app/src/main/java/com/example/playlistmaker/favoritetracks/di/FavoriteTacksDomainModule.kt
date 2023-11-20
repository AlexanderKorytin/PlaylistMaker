package com.example.playlistmaker.favoritetracks.di

import com.example.playlistmaker.favoritetracks.data.db.repository.FavoriteTracksDatabaseRepositoryImpl
import com.example.playlistmaker.favoritetracks.domain.FavoriteTracksDatabaseRepository
import org.koin.dsl.module

val favoriteTracksDomainModule = module {

    single<FavoriteTracksDatabaseRepository> {
        FavoriteTracksDatabaseRepositoryImpl(appDatabase = get(), converter = get())
    }

}