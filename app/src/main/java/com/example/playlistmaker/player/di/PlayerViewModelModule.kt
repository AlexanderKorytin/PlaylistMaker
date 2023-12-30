package com.example.playlistmaker.player.di

import com.example.playlistmaker.player.ui.mappers.MapClickedTrackGsonToClickedTrack
import com.example.playlistmaker.player.ui.models.ClickedTrackGson
import com.example.playlistmaker.player.ui.viewmodel.MediaPlayerViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerViewModelModule = module {

    factory<MapClickedTrackGsonToClickedTrack> {
        MapClickedTrackGsonToClickedTrack(json = get())
    }
    factory {
        FirebaseAnalytics.getInstance(androidContext())
    }

    viewModel<MediaPlayerViewModel> { (track: ClickedTrackGson) ->
        MediaPlayerViewModel(
            clickedTrack = track,
            mediaPlayerInteractor = get(),
            clickedTrackConverter = get(),
            favoriteTracksInteractor = get(),
            playListInteractor = get(),
            currentPlayListInteractor = get(),
            analytics = get()
        )
    }
}