package com.example.playlistmaker.player.di

import com.example.playlistmaker.player.ui.mappers.MapClickedTrackGsonToClickedTrack
import com.example.playlistmaker.player.ui.models.ClickedTrackGson
import com.example.playlistmaker.player.ui.viewmodel.MediaPlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerViewModelModule = module {

    factory<MapClickedTrackGsonToClickedTrack> {
        MapClickedTrackGsonToClickedTrack(json = get())
    }

    viewModel<MediaPlayerViewModel> { (track: ClickedTrackGson) ->
        MediaPlayerViewModel(
            clickedTrack = track,
            mediaPlayerInteractor = get(),
            handlerMain = get(),
            getClicketTrack = get()
        )
    }
}