package com.example.playlistmaker.Util

import android.content.Context
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.playlistmaker.settings.domain.api.SettingsRepository
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.api.ExternalNavigator
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl

object Creator {

    fun provideGetSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(provideGetExternalNavigation(context))
    }

    private fun provideGetExternalNavigation(context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

    fun provideGetSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(provideGetSettingsRepository(context))
    }

    private fun provideGetSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }
}