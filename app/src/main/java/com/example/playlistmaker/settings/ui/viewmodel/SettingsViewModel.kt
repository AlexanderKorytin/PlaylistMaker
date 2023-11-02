package com.example.playlistmaker.settings.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.playlistmaker.settings.domain.model.ThemeSettings
import com.example.playlistmaker.sharing.domain.api.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {

    private val darkThemeChecked = MutableLiveData<ThemeSettings>()

    private val liveDataAgr = MutableLiveData<String>()

    fun getAgreementLink(): LiveData<String> = liveDataAgr

    fun getCurrentTheme(): LiveData<ThemeSettings> = darkThemeChecked

    fun updateNightTheme(checked: Boolean) {
        settingsInteractor.updateThemeSetting(ThemeSettings(checked))
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun writeToSupport() {
        sharingInteractor.openSupport()
    }

    fun seeUserAgreement() {
        liveDataAgr.value = sharingInteractor.openTerms()
    }
}