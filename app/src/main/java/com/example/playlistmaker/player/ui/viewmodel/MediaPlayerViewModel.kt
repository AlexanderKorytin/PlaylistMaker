package com.example.playlistmaker.player.ui.viewmodel

import android.icu.text.SimpleDateFormat
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.ui.models.ClickedTrack
import com.example.playlistmaker.player.ui.models.MediaPlayerScreenState
import java.util.Locale

class MediaPlayerViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val clickedTrack: ClickedTrack,
) : ViewModel() {
    companion object {
        private const val UPDATE_TIMER_TRACK = 300L
    }

    private val handlerMain = Handler(Looper.getMainLooper())

    private var mediaPlayerCurrentTimePlaying =
        SimpleDateFormat(
            "mm:ss", Locale.getDefault()
        ).format(mediaPlayerInteractor.getTimerStart())


    private var currentTrack = MutableLiveData<ClickedTrack>(clickedTrack)
    fun getCurrentTrack(): LiveData<ClickedTrack> = currentTrack

    private var playerScreenState = MutableLiveData<MediaPlayerScreenState>(MediaPlayerScreenState(mediaPlayerCurrentTimePlaying, mediaPlayerInteractor.getPlayerState()))
    fun getPlayerScreenState(): LiveData<MediaPlayerScreenState> = playerScreenState

    init {
        playerScreenState.value = MediaPlayerScreenState(mediaPlayerCurrentTimePlaying, mediaPlayerInteractor.getPlayerState())
    }

    private fun startPlayer() {
        val currentPlayerStateState = getPlayerScreenState()
        mediaPlayerInteractor.play()
        handlerMain.post(updateTimerMedia())
        playerScreenState.value = currentPlayerStateState.value?.copy(playerState = mediaPlayerInteractor.getPlayerState())

    }

    fun pausePlayer() {
        val currentPlayerStateState = getPlayerScreenState()
        mediaPlayerInteractor.pause()
        handlerMain.removeCallbacks(updateTimerMedia())
        playerScreenState.value= currentPlayerStateState.value?.copy(playerState = mediaPlayerInteractor.getPlayerState())
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayerInteractor.release()
    }

    private fun updateTimerMedia(): Runnable {
        return object : Runnable {
            override fun run() {
                if (mediaPlayerInteractor.getPlayerState() == PlayerState.STATE_PLAYING){
                    val currentTime = SimpleDateFormat(
                        "mm:ss", Locale.getDefault()
                    ).format(mediaPlayerInteractor.getCurrentPosition())
                    playerScreenState.value = MediaPlayerScreenState(currentTime, mediaPlayerInteractor.getPlayerState())
                    handlerMain.postDelayed(this, UPDATE_TIMER_TRACK)
                }
               if (mediaPlayerInteractor.getPlayerState() == PlayerState.STATE_PREPARED){
                   val currentTime = SimpleDateFormat(
                       "mm:ss", Locale.getDefault()
                   ).format(mediaPlayerInteractor.getTimerStart())
                   playerScreenState.value = MediaPlayerScreenState(currentTime, mediaPlayerInteractor.getPlayerState())
                   handlerMain.post(this)
               }
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
}