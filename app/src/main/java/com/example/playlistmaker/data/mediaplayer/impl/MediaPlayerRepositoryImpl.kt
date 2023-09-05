package com.example.playlistmaker.data.mediaplayer.impl

import android.media.MediaPlayer
import com.example.playlistmaker.data.dto.TrackUrl
import com.example.playlistmaker.domain.api.MediaPlayerRepository
import com.example.playlistmaker.domain.models.PlayerState

class MediaPlayerRepositoryImpl(
    private val urlTrack: TrackUrl
) : MediaPlayerRepository {
    private var mediaPlayer = MediaPlayer()
    override var playerState = PlayerState.STATE_DEFAULT
    private val timerStart = 0L

    init {
        preparePlayer()
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(urlTrack.url)
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.STATE_PREPARED
        }

        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.STATE_PREPARED
        }
    }

    override fun play() {
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING
    }

    override fun pause() {
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun getTimerStart(): Long {
        return timerStart
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

}