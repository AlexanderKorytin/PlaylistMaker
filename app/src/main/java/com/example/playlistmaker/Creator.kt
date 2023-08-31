package com.example.playlistmaker

import com.example.playlistmaker.data.dto.TrackUrl
import com.example.playlistmaker.data.mediaplayer.impl.MediaPlayerDataImpl
import com.example.playlistmaker.domain.api.MediaPlayerData
import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.impl.MediaPlayerInteractorImpl

object Creator {
    lateinit var url: TrackUrl
    fun provideGetMediplayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(provideGetMediplayerData())
    }

    fun provideGetMediplayerData(): MediaPlayerData {
        return MediaPlayerDataImpl(url)
    }
}