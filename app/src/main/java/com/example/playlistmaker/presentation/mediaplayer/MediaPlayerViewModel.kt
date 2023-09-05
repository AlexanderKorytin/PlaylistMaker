package com.example.playlistmaker.presentation.mediaplayer

import android.icu.text.SimpleDateFormat
import android.os.Handler
import android.os.Looper
import androidx.constraintlayout.widget.Placeholder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.impl.ImageLoaderUseCase
import com.example.playlistmaker.domain.models.PlayerState
import com.example.playlistmaker.presentation.models.ClickedTrack
import java.util.Locale

class MediaPlayerViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val imageLoaderUseCase: ImageLoaderUseCase,
    private val clickedTrack: ClickedTrack,
    private val roundCornersTrackImage: Int
) : ViewModel() {
    companion object {
        private const val UPDATE_TIMER_TRACK = 300L
    }

    private val handlerMain = Handler(Looper.getMainLooper())

    private var mediaPlayerCurrentTimePlaying = MutableLiveData<String>(
        SimpleDateFormat(
            "mm:ss", Locale.getDefault()
        ).format(mediaPlayerInteractor.getTimerStart())
    )

    fun getTimerCurrentPositionMediaPlayer(): LiveData<String> = mediaPlayerCurrentTimePlaying

    private var currentTrack = MutableLiveData<ClickedTrack>(clickedTrack)
    fun getCurrentTrack(): LiveData<ClickedTrack> = currentTrack


    private var currentPlayerState =
        MutableLiveData<PlayerState>(mediaPlayerInteractor.getPlayerState())

    fun getCurrentPlayerState() = currentPlayerState

    private fun startPlayer() {
        mediaPlayerInteractor.play()
        handlerMain.post(updateTimerMedia())
        currentPlayerState.value = mediaPlayerInteractor.getPlayerState()

    }

    fun pausePlayer() {
        mediaPlayerInteractor.pause()
        handlerMain.removeCallbacks(updateTimerMedia())
        currentPlayerState.value = mediaPlayerInteractor.getPlayerState()
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayerInteractor.release()
    }

    fun showTrackAlbumImage(placeholder: Int, round: Int) {
        imageLoaderUseCase.execute(
            clickedTrack.coverArtWork,
            placeholder,
            round
        )
    }

    private fun updateTimerMedia(): Runnable {
        return object : Runnable {
            override fun run() {
                if (mediaPlayerInteractor.getPlayerState() == PlayerState.STATE_PLAYING) {
                    mediaPlayerCurrentTimePlaying.postValue(
                        SimpleDateFormat(
                            "mm:ss", Locale.getDefault()
                        ).format(mediaPlayerInteractor.getCurrentPosition())
                    )
                    handlerMain.postDelayed(this, UPDATE_TIMER_TRACK)
                }
                if (mediaPlayerInteractor.getPlayerState() == PlayerState.STATE_PREPARED) {
                    mediaPlayerCurrentTimePlaying.postValue(
                        SimpleDateFormat(
                            "mm:ss", Locale.getDefault()
                        ).format(mediaPlayerInteractor.getTimerStart())
                    )
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