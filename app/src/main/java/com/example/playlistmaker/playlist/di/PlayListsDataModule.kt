package com.example.playlistmaker.playlist.di

import com.example.playlistmaker.playlist.data.db.converter.PlayListDbConverter
import org.koin.dsl.module

val playListsDataModule = module {
    factory<PlayListDbConverter> { PlayListDbConverter() }
}