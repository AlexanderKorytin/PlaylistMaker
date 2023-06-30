package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val APP_SETTINGS = "App settings"
const val DARK_THEME = "dark_theme"

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val switchPreference = getSharedPreferences(APP_SETTINGS, MODE_PRIVATE)
        darkTheme = switchPreference.getBoolean(DARK_THEME, darkTheme)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}