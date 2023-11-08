package com.example.playlistmaker.player.ui.viewmodel

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.models.ClickedTrack
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.ui.mappers.MapClickedTrackGsonToClickedTrack
import com.example.playlistmaker.player.ui.models.ClickedTrackGson
import com.example.playlistmaker.player.ui.models.MediaPlayerScreenState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class MediaPlayerViewModel(
    val clickedTrack: ClickedTrackGson,
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    getClicketTrack: MapClickedTrackGsonToClickedTrack
) : ViewModel() {

    val playedTrack = getClicketTrack.map(clickedTrack)

    private var timerJob: Job? = null

    private fun preparePlayer() {
        mediaPlayerInteractor.prepare(playedTrack)
        checkingPreparePlayer()
    }


    private var mediaPlayerCurrentTimePlaying =
        SimpleDateFormat(
            "mm:ss", Locale.getDefault()
        ).format(mediaPlayerInteractor.getTimerStart())


    private var currentTrack = MutableLiveData<ClickedTrack>(getClicketTrack.map(clickedTrack))
    fun getCurrentTrack(): LiveData<ClickedTrack> = currentTrack

    private var playerScreenState = MutableLiveData<MediaPlayerScreenState>(
        MediaPlayerScreenState(
            mediaPlayerCurrentTimePlaying,
            mediaPlayerInteractor.getPlayerState()
        )
    )

    fun getPlayerScreenState(): LiveData<MediaPlayerScreenState> = playerScreenState

    init {
        playerScreenState.value = MediaPlayerScreenState(
            mediaPlayerCurrentTimePlaying,
            mediaPlayerInteractor.getPlayerState()
        )
        preparePlayer()
    }

    private fun startPlayer() {
        val currentPlayerStateState = getPlayerScreenState()
        mediaPlayerInteractor.play()
        updateTimerMedia()
        playerScreenState.value =
            currentPlayerStateState.value?.copy(playerState = mediaPlayerInteractor.getPlayerState())

    }

    fun pausePlayer() {
        val currentPlayerStateState = getPlayerScreenState()
        mediaPlayerInteractor.pause()
        timerJob?.cancel()
        playerScreenState.value =
            currentPlayerStateState.value?.copy(playerState = mediaPlayerInteractor.getPlayerState())
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayerInteractor.release()
    }

    private fun updateTimerMedia() {
        viewModelScope.launch {
            while (mediaPlayerInteractor.getPlayerState() == PlayerState.STATE_PLAYING) {
                delay(UPDATE_TIMER_TRACK)
                val currentTime = SimpleDateFormat(
                    "mm:ss", Locale.getDefault()
                ).format(mediaPlayerInteractor.getCurrentPosition())
                playerScreenState.value =
                    MediaPlayerScreenState(currentTime, mediaPlayerInteractor.getPlayerState())
            }
            if (mediaPlayerInteractor.getPlayerState() == PlayerState.STATE_PREPARED) {
                val currentTime = SimpleDateFormat(
                    "mm:ss", Locale.getDefault()
                ).format(mediaPlayerInteractor.getTimerStart())
                playerScreenState.value =
                    MediaPlayerScreenState(currentTime, mediaPlayerInteractor.getPlayerState())
            }
        }
    }

    private fun checkingPreparePlayer() {

        if (mediaPlayerInteractor.getPlayerState() != PlayerState.STATE_PREPARED) {
            viewModelScope.launch {
                while (mediaPlayerInteractor.getPlayerState() != PlayerState.STATE_PREPARED) {
                    delay(UPDATE_TIMER_TRACK)
                }
                playerScreenState.value = MediaPlayerScreenState(
                    mediaPlayerCurrentTimePlaying,
                    mediaPlayerInteractor.getPlayerState()
                )
            }
        }

    }

    fun playbackControl() {
        when (mediaPlayerInteractor.getPlayerState()) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
            }

            else -> {}
        }
    }

    companion object {
        private const val UPDATE_TIMER_TRACK = 300L
    }
}