package com.example.playlistmaker.settings.data.impl

import android.content.Context
import com.example.playlistmaker.settings.domain.api.SettingsRepository
import com.example.playlistmaker.settings.domain.model.ThemeSettings

class SettingsRepositoryImpl(private val context: Context): SettingsRepository {
    override fun getThemeSettings(): ThemeSettings {
        TODO("Not yet implemented")
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        TODO("Not yet implemented")
    }
}