package com.example.playlistmaker.currentplaylist.di

import com.example.playlistmaker.currentplaylist.data.db.converter.CurrentPlayListTrackDBConverter
import org.koin.dsl.module

val currentPlaListDataModule = module {
    factory { CurrentPlayListTrackDBConverter() }
}