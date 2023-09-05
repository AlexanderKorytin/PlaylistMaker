package com.example.playlistmaker.presentation.mediaplayer

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.Creator
import com.example.playlistmaker.presentation.models.ClickedTrack

class MediaPlayerViewModelFactory(
    private val clickedTrack: ClickedTrack,
    image: ImageView,
    private val round: Int
) : ViewModelProvider.Factory {
    val mediaPlayerInteractor = Creator.provideGetMediplayerInteractor()
    val imageLoaderUseCase = Creator.provideGetImageLoaderUseCase(image)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MediaPlayerViewModel(
            mediaPlayerInteractor,
            imageLoaderUseCase,
            clickedTrack,
            round
        ) as T
    }
}