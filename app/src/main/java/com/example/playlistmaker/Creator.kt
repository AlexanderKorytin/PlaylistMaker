package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.ImageLoaderGlide
import com.example.playlistmaker.data.dto.TrackUrl
import com.example.playlistmaker.data.mediaplayer.impl.MediaPlayerDataImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.SearchHistoryImpl
import com.example.playlistmaker.data.network.TracksRepositoryImpl
import com.example.playlistmaker.domain.api.ImageLoader
import com.example.playlistmaker.domain.api.MediaPlayerData
import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.api.SearchHistory
import com.example.playlistmaker.domain.api.SetViewVisibilityUseCase
import com.example.playlistmaker.domain.api.GetTracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.ClearButtonSetViewVisibilityUseCase
import com.example.playlistmaker.domain.impl.ImageLoaderUseCase
import com.example.playlistmaker.domain.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.GetTracksInteractorImpl

object Creator {
    lateinit var url: TrackUrl
    lateinit var sharedPreferences: SharedPreferences
    fun provideGetMediplayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(provideGetMediplayerData())
    }

    private fun provideGetMediplayerData(): MediaPlayerData {
        return MediaPlayerDataImpl(url)
    }

    fun provideGetTrackInteractor(): GetTracksInteractor {
        return GetTracksInteractorImpl(provideGetTracksRepository())
    }

    private fun provideGetTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(provideGetNetworkClient())
    }

    private fun provideGetNetworkClient(): NetworkClient {
        return RetrofitNetworkClient()
    }

    fun provideGetImageLoaderUseCase(): ImageLoaderUseCase {
        return ImageLoaderUseCase(provideGetImageLoader())
    }

    private fun provideGetImageLoader(): ImageLoader {
        return ImageLoaderGlide()
    }

    fun provideGetSetViewVisibilityUseCase(): SetViewVisibilityUseCase{
        return  ClearButtonSetViewVisibilityUseCase()
    }

    fun provideGetSearchHistoryInteractor(): SearchHistoryInteractorImpl {
        return  SearchHistoryInteractorImpl(provideGetSearchHistory())
    }
    private fun provideGetSearchHistory(): SearchHistory{
        return SearchHistoryImpl(sharedPreferences)
    }
}