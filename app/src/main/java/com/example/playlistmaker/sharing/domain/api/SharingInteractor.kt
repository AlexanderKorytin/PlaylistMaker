package com.example.playlistmaker.sharing.domain.api

import com.example.playlistmaker.sharing.domain.models.EmailData

interface SharingInteractor {
    fun shareApp()
    fun openTerms()
    fun openSupport()
}