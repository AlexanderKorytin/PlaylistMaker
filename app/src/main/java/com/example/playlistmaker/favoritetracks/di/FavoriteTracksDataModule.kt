package com.example.playlistmaker.favoritetracks.di

import androidx.room.Room
import com.example.playlistmaker.favorite.data.db.AppDataBase
import com.example.playlistmaker.favorite.data.db.converter.TrackDbConverter
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val favoriteTracksDataModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDataBase::class.java, "favorite_tracks.db").build()
    }

    factory<TrackDbConverter> {
        TrackDbConverter()
    }
}