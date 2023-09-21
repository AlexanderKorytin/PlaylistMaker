package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.data.dto.TrackUrl
import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.api.MediaPlayerRepository
import com.example.playlistmaker.player.domain.models.PlayerState


class MediaPlayerInteractorImpl(private val mediaPlayerRepository: MediaPlayerRepository) :
    MediaPlayerInteractor {
    override fun prepare(url: TrackUrl) {
        mediaPlayerRepository.preparePlayer(url)
    }

    override fun play() {
        mediaPlayerRepository.play()
    }

    override fun pause() {
        mediaPlayerRepository.pause()
    }

    override fun release() {
        mediaPlayerRepository.release()
    }

    override fun getTimerStart(): Long {
        return mediaPlayerRepository.getTimerStart()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayerRepository.getCurrentPosition()
    }

    override fun getPlayerState(): PlayerState {
        return mediaPlayerRepository.playerState
    }
}