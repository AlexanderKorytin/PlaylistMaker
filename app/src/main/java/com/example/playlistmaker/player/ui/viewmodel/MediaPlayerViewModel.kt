package com.example.playlistmaker.player.ui.viewmodel

import android.icu.text.SimpleDateFormat
import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.models.ClickedTrack
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.ui.mappers.MapClickedTrackGsonToClickedTrack
import com.example.playlistmaker.player.ui.models.ClickedTrackGson
import com.example.playlistmaker.player.ui.models.MediaPlayerScreenState
import java.util.Locale

class MediaPlayerViewModel(
    val clickedTrack: ClickedTrackGson,
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    val handlerMain: Handler,
    getClicketTrack: MapClickedTrackGsonToClickedTrack
) : ViewModel() {
    companion object {
        private const val UPDATE_TIMER_TRACK = 300L
    }

    val playedTrack = getClicketTrack.map(clickedTrack)

    private fun preparePlayer() {
        mediaPlayerInteractor.prepare(playedTrack)
       handlerMain.post (checkedPreparePlayer() )
    }


    private var mediaPlayerCurrentTimePlaying =
        SimpleDateFormat(
            "mm:ss", Locale.getDefault()
        ).format(mediaPlayerInteractor.getTimerStart())


    private var currentTrack = MutableLiveData<ClickedTrack>(getClicketTrack.map(clickedTrack))
    fun getCurrentTrack(): LiveData<ClickedTrack> = currentTrack

    private var playerScreenState = MutableLiveData<MediaPlayerScreenState>(
        MediaPlayerScreenState(mediaPlayerCurrentTimePlaying, mediaPlayerInteractor.getPlayerState())
    )

    fun getPlayerScreenState(): LiveData<MediaPlayerScreenState> = playerScreenState

    init {
        playerScreenState.value = MediaPlayerScreenState(
            mediaPlayerCurrentTimePlaying,
            mediaPlayerInteractor.getPlayerState())
        preparePlayer()
    }

    private fun startPlayer() {
        val currentPlayerStateState = getPlayerScreenState()
        mediaPlayerInteractor.play()
        handlerMain.post(updateTimerMedia())
        playerScreenState.value =
            currentPlayerStateState.value?.copy(playerState = mediaPlayerInteractor.getPlayerState())

    }

    fun pausePlayer() {
        val currentPlayerStateState = getPlayerScreenState()
        mediaPlayerInteractor.pause()
        handlerMain.removeCallbacks(updateTimerMedia())
        playerScreenState.value =
            currentPlayerStateState.value?.copy(playerState = mediaPlayerInteractor.getPlayerState())
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayerInteractor.release()
    }

    private fun updateTimerMedia(): Runnable {
        return object : Runnable {
            override fun run() {
                if (mediaPlayerInteractor.getPlayerState() == PlayerState.STATE_PLAYING) {
                    val currentTime = SimpleDateFormat(
                        "mm:ss", Locale.getDefault()
                    ).format(mediaPlayerInteractor.getCurrentPosition())
                    playerScreenState.value =
                        MediaPlayerScreenState(currentTime, mediaPlayerInteractor.getPlayerState())
                    handlerMain.postDelayed(this, UPDATE_TIMER_TRACK)
                }
                if (mediaPlayerInteractor.getPlayerState() == PlayerState.STATE_PREPARED) {
                    val currentTime = SimpleDateFormat(
                        "mm:ss", Locale.getDefault()
                    ).format(mediaPlayerInteractor.getTimerStart())
                    playerScreenState.value =
                        MediaPlayerScreenState(currentTime, mediaPlayerInteractor.getPlayerState())
                    handlerMain.post(this)
                }
            }
        }
    }

    private fun checkedPreparePlayer() : Runnable{
       return object: Runnable{
           override fun run() {
               if (mediaPlayerInteractor.getPlayerState() != PlayerState.STATE_PREPARED){
                   handlerMain.postDelayed(this, UPDATE_TIMER_TRACK)
               } else {
                   handlerMain.removeCallbacks(this)
                   playerScreenState.value =  MediaPlayerScreenState(mediaPlayerCurrentTimePlaying, mediaPlayerInteractor.getPlayerState())
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