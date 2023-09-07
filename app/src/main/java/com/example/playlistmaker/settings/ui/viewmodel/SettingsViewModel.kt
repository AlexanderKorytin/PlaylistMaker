package com.example.playlistmaker.settings.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.models.EmailData

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {

    fun shareApp(){
        sharingInteractor.shareApp()
    }
    fun writeToSupport(){
        sharingInteractor.openSupport()
    }
    fun seeUserAgreement(){
        sharingInteractor.openTerms()
    }
}