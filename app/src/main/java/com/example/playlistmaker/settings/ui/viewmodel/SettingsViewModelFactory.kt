package com.example.playlistmaker.settings.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.Util.App

class SettingsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    val sharingInteractor by lazy { App().creator.provideGetSharingInteractor(context) }
    val settingsInteractor by lazy { App().creator.provideGetSettingsInteractor(context) }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return return SettingsViewModel(
            sharingInteractor = sharingInteractor,
            settingsInteractor = settingsInteractor,
        ) as T
    }
}