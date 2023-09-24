package com.example.playlistmaker.settings.di

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.app.APP_SETTINGS_PREF_KEY
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val settingsDataModule = module {

    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            APP_SETTINGS_PREF_KEY,
            AppCompatActivity.MODE_PRIVATE
        )
    }

}