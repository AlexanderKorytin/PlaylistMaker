package com.example.playlistmaker.util

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.player.di.playerDomainModule
import com.example.playlistmaker.player.di.playerViewModelModule
import com.example.playlistmaker.search.di.searchDataModule
import com.example.playlistmaker.search.di.searchDomainModule
import com.example.playlistmaker.search.di.searchViewModelModule
import com.example.playlistmaker.settings.di.settingsDataModule
import com.example.playlistmaker.settings.di.settingsDomainModule
import com.example.playlistmaker.settings.di.settingsViewModelModule
import com.example.playlistmaker.sharing.di.sharingDomainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

const val APP_SETTINGS_PREF_KEY = "App settings"
const val DARK_THEME = "dark_theme"

class App : Application() {


    private var darkTheme: Boolean = false

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                searchViewModelModule,
                searchDomainModule,
                searchDataModule,

                playerViewModelModule,
                playerDomainModule,

                settingsDataModule,
                settingsDomainModule,
                sharingDomainModule,
                settingsViewModelModule,
            )
        }
        val switchPreference = getSharedPreferences(APP_SETTINGS_PREF_KEY, MODE_PRIVATE)
        val darkThemeEnabled = switchPreference.getBoolean(DARK_THEME, darkTheme)
        switchTheme(darkThemeEnabled)
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