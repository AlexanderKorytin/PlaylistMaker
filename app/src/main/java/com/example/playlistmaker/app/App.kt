package com.example.playlistmaker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.currentplaylist.di.currentPlaListDataModule
import com.example.playlistmaker.currentplaylist.di.currentPlayListDomainModule
import com.example.playlistmaker.favoritetracks.di.favoriteTracksDataModule
import com.example.playlistmaker.favoritetracks.di.favoriteTracksDomainModule
import com.example.playlistmaker.favoritetracks.di.favoriteTracksViewModelViewModelModule
import com.example.playlistmaker.player.di.playerDomainModule
import com.example.playlistmaker.player.di.playerViewModelModule
import com.example.playlistmaker.playlist.di.playListsDataModule
import com.example.playlistmaker.playlist.di.playListsDomainModule
import com.example.playlistmaker.playlists.di.playListsViewModelModule
import com.example.playlistmaker.search.di.searchDataModule
import com.example.playlistmaker.search.di.searchDomainModule
import com.example.playlistmaker.search.di.searchViewModelModule
import com.example.playlistmaker.settings.di.settingsDataModule
import com.example.playlistmaker.settings.di.settingsDomainModule
import com.example.playlistmaker.settings.di.settingsViewModelModule
import com.example.playlistmaker.sharing.di.sharingDomainModule
import com.markodevcic.peko.PermissionRequester
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

const val APP_SETTINGS_PREF_KEY = "App settings"
const val IS_FIRST_START = "first_start"
const val DARK_THEME = "dark_theme"

class App : Application() {


    private var darkTheme: Boolean = false
    private var isfirstStart = true
    private var firstStart: Boolean = true

    override fun onCreate() {
        super.onCreate()

        PermissionRequester.initialize(applicationContext)

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

                playListsViewModelModule,
                playListsDataModule,
                playListsDomainModule,

                favoriteTracksViewModelViewModelModule,
                favoriteTracksDataModule,
                favoriteTracksDomainModule,

                currentPlaListDataModule,
                currentPlayListDomainModule,

            )
        }
        val switchPreference = getSharedPreferences(APP_SETTINGS_PREF_KEY, MODE_PRIVATE)
        val darkThemeEnabled: Boolean = switchPreference.getBoolean(DARK_THEME, darkTheme)

        isfirstStart = switchPreference.getBoolean(IS_FIRST_START, true)
        if (isfirstStart) switchPreference.edit().putBoolean(IS_FIRST_START, false).apply()

        switchTheme(darkThemeEnabled)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        if (isfirstStart) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            isfirstStart = false
        } else {
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
}