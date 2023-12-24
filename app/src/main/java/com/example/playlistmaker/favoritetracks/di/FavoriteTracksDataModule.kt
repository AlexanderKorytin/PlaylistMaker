package com.example.playlistmaker.favoritetracks.di

import androidx.room.Room
import com.example.playlistmaker.core.db.AppDataBase
import com.example.playlistmaker.favoritetracks.db.converter.TrackDbConverter
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val favoriteTracksDataModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDataBase::class.java, "app_database.db").build()
    }

    factory<TrackDbConverter> {
        TrackDbConverter(json = get())
    }
}