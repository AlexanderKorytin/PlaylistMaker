package com.example.playlistmaker.data.mediaplayer.impl

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.R
import com.example.playlistmaker.data.dto.PlayerState
import com.example.playlistmaker.data.dto.TrackUrl
import com.example.playlistmaker.data.mediaplayer.api.MediaPlayerInteractor
import com.example.playlistmaker.data.mediaplayer.mapper.MediaPlayerMapper
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.domain.models.ClickedTrack
import java.util.Locale


private const val UPDATE_TIMER_TRACK = 300L

class MediaPlayerInteractorImpl(
    clickedTrack: ClickedTrack,
    private val binding: ActivityMediaBinding,
    private val context: Context
) : MediaPlayerInteractor {
    private val mediaPlayerMapper = MediaPlayerMapper()
    private var mediaPlayer = MediaPlayer()
    private val handlerMain = Handler(Looper.getMainLooper())
    var playerState = PlayerState.STATE_DEFAULT
    private var timerStart = 0L
    private val trackUrl = mediaPlayerMapper.toMediaPlayer(clickedTrack)

    init {
        preparePlayer()
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(trackUrl.url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.playPause.isEnabled = true
            playerState = PlayerState.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            binding.playPause.setImageDrawable(context.getDrawable(R.drawable.play_button))
            binding.timerMedia.text = SimpleDateFormat(
                "mm:ss", Locale.getDefault()
            ).format(timerStart)
            playerState = PlayerState.STATE_PREPARED
        }
    }

    override fun play() {
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING
        binding.playPause.setImageDrawable(context.getDrawable(R.drawable.pause_button))
        handlerMain.post(updateTimerMedia())
    }

    override fun pause() {
        handlerMain.removeCallbacks(updateTimerMedia())
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PLAYING
        playerState = PlayerState.STATE_PAUSED
        binding.playPause.setImageDrawable(context.getDrawable(R.drawable.play_button))
    }

    override fun release() {
        mediaPlayer.release()
    }

    fun getTimerStart(): Long {
        return timerStart
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition

    }

    private fun updateTimerMedia(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == PlayerState.STATE_PLAYING) {
                    binding.timerMedia.text = SimpleDateFormat(
                        "mm:ss", Locale.getDefault()
                    ).format(mediaPlayer.currentPosition)
                    handlerMain.postDelayed(this, UPDATE_TIMER_TRACK)
                }
            }

        }
    }

    override fun playbackControl() {
        when (playerState) {
            PlayerState.STATE_PLAYING -> {
                pause()
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                play()
            }

            else -> {}
        }
    }
}