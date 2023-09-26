package com.example.playlistmaker.settings.di

import com.example.playlistmaker.app.App
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.playlistmaker.settings.domain.api.SettingsRepository
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val settingsDomainModule = module {

    factory<SettingsRepository> {
        SettingsRepositoryImpl(switchPreferences = get(), app = androidApplication() as App)
    }

    factory<SettingsInteractor> {
        SettingsInteractorImpl(settingsRepository = get())
    }

}