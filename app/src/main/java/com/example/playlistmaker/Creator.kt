package com.example.playlistmaker

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.TrackUrl
import com.example.playlistmaker.data.mediaplayer.impl.MediaPlayerDataImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.TracksRepositoryImpl
import com.example.playlistmaker.domain.api.MediaPlayerData
import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    lateinit var url: TrackUrl
    fun provideGetMediplayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(provideGetMediplayerData())
    }

    fun provideGetMediplayerData(): MediaPlayerData {
        return MediaPlayerDataImpl(url)
    }

    fun provideGetTrackInteractor(): TracksInteractor {
        return TracksInteractorImpl(provideGetTracksRepository())
    }

    fun provideGetTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(provideGetNetworkClient())
    }

    fun provideGetNetworkClient(): NetworkClient{
        return RetrofitNetworkClient()
    }
}