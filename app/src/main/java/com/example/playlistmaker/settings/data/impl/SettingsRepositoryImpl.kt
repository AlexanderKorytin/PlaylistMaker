package com.example.playlistmaker.settings.data.impl

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.Util.APP_SETTINGS_PREF_KEY
import com.example.playlistmaker.Util.App
import com.example.playlistmaker.Util.DARK_THEME
import com.example.playlistmaker.settings.domain.api.SettingsRepository
import com.example.playlistmaker.settings.domain.model.ThemeSettings

class SettingsRepositoryImpl(private val context: Context) : SettingsRepository {
    val switchPreference = context.getSharedPreferences(
        APP_SETTINGS_PREF_KEY,
        AppCompatActivity.MODE_PRIVATE
    )

    override fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(switchPreference.getBoolean(DARK_THEME, false))
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        App().switchTheme(settings.isNight)
        switchPreference.edit()
            .putBoolean(DARK_THEME, settings.isNight)
            .apply()
    }
}