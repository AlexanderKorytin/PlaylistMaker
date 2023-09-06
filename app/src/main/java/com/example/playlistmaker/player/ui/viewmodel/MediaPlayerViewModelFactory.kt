package com.example.playlistmaker.player.ui.viewmodel

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.Util.Creator
import com.example.playlistmaker.player.ui.models.ClickedTrack

class MediaPlayerViewModelFactory(
    private val clickedTrack: ClickedTrack,
    image: ImageView,
    private val round: Int
) : ViewModelProvider.Factory {
    val mediaPlayerInteractor = Creator.provideGetMediplayerInteractor()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MediaPlayerViewModel(
            mediaPlayerInteractor,
            clickedTrack,
        ) as T
    }
}