package com.example.playlistmaker.Util

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.player.data.dto.TrackUrl
import com.example.playlistmaker.player.data.impl.MediaPlayerRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.network.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.network.TracksRepositoryImpl
import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.api.SetViewVisibilityUseCase
import com.example.playlistmaker.search.domain.api.SearchTracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.impl.ClearButtonSetViewVisibilityUseCase
import com.example.playlistmaker.player.domain.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.SearchTracksInteractorImpl
import com.example.playlistmaker.player.domain.api.MediaPlayerRepository

object Creator {
    lateinit var url: TrackUrl
    lateinit var sharedPreferences: SharedPreferences
    fun provideGetMediplayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(provideGetMediplayerData())
    }

    private fun provideGetMediplayerData(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl(url)
    }

    fun provideGetTrackInteractor(context: Context): SearchTracksInteractor {
        return SearchTracksInteractorImpl(provideGetTracksRepository(context = context))
    }

    private fun provideGetTracksRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(provideGetNetworkClient(context = context))
    }

    private fun provideGetNetworkClient(context: Context): NetworkClient {
        return RetrofitNetworkClient(context = context)
    }

    fun provideGetSetViewVisibilityUseCase(): SetViewVisibilityUseCase {
        return  ClearButtonSetViewVisibilityUseCase()
    }

    fun provideGetSearchHistoryInteractor(context: Context): SearchHistoryInteractorImpl {
        return  SearchHistoryInteractorImpl(provideGetSearchHistoryRepository(context))
    }
    private fun provideGetSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(context)
    }
}