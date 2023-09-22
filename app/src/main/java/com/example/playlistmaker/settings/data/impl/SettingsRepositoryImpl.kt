package com.example.playlistmaker.settings.data.impl

import android.content.SharedPreferences
import com.example.playlistmaker.Util.App
import com.example.playlistmaker.Util.DARK_THEME
import com.example.playlistmaker.settings.domain.api.SettingsRepository
import com.example.playlistmaker.settings.domain.model.ThemeSettings

class SettingsRepositoryImpl(
    private val switchPreferences: SharedPreferences
) : SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(switchPreferences.getBoolean(DARK_THEME, false))
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        App().switchTheme(settings.isNight)
        switchPreferences.edit()
            .putBoolean(DARK_THEME, settings.isNight)
            .apply()
    }

}
