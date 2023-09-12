package com.example.playlistmaker.player.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.Util.App
import com.example.playlistmaker.player.ui.models.ClickedTrack

class MediaPlayerViewModelFactory(
    private val clickedTrack: ClickedTrack,
) : ViewModelProvider.Factory {
    val mediaPlayerInteractor = App().creator.provideGetMediplayerInteractor()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MediaPlayerViewModel(
            mediaPlayerInteractor,
            clickedTrack,
        ) as T
    }
}