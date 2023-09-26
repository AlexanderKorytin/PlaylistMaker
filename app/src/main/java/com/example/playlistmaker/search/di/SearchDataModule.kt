package com.example.playlistmaker.search.di

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.network.ITunesApi
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TRACK_HISTORY_SHAREDPREFERENCES = "Track history"

val searchDataModule = module {

    single<ITunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(androidContext(), iTunesService = get())
    }

    factory { Gson() }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            TRACK_HISTORY_SHAREDPREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        )
    }


}