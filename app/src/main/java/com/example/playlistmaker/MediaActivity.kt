package com.example.playlistmaker

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.google.gson.Gson
import java.util.Locale


class MediaActivity : AppCompatActivity() {

    companion object {
        private const val CLICKED_TRACK = "CLICKED_TRACK"
        private const val cornersRatio = 120
        private const val UPDATE_TIMER_TRACK = 300L
    }

    private var receivedTrack: String? = null
    private var widthDisplay = 0
    private var roundedCorners = 0
    private lateinit var binding: ActivityMediaBinding
    private var timerStart = 0L
    private var timerStop = 0L
    private lateinit var outAnim: Animation
    private lateinit var inAnim: Animation

    // инициализируем медиа плеер
    // и задаем его состояние по умолчанию
    private var mediaPlayer = MediaPlayer()
    var playerState = PlayerState.STATE_DEFAULT
    private var trackUrl: String? = null

    private var handlerMain: Handler? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CLICKED_TRACK, receivedTrack)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        handlerMain = Handler(Looper.getMainLooper())
        // вычисляем радиус углов от реального размера картинки исходя из параметров верстки радиус 8
        // при высоте дисплея 832 из которых обложка - 312
        // коэффициент примерно - 1/120
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        widthDisplay = displayMetrics.widthPixels
        roundedCorners = (dpToPx(widthDisplay.toFloat(), this)) / cornersRatio

        receivedTrack = savedInstanceState?.getString(CLICKED_TRACK, "")
            ?: intent.getStringExtra("clickedTrack")
        val clickedTrack = Gson().fromJson(receivedTrack, Track::class.java)
        filledTrackMeans(clickedTrack)
        trackUrl = clickedTrack.previewUrl


        //загружаем анимации
        inAnim = AnimationUtils.loadAnimation(this, R.anim.fadein)
        outAnim = AnimationUtils.loadAnimation(this, R.anim.fadeout)
        //добавляем слушателя на анимацию
        outAnim.setAnimationListener(object : AnimationListener{
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                playbackControl()
                binding.playPause.startAnimation(inAnim)
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })
        inAnim.setAnimationListener(object: AnimationListener{
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                setVibe()
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })

        preparePlayer()
// Подготовка плеера и установка слушателей
        binding.backMedia.setOnClickListenerWithViber {
            finish()
        }

        binding.playPause.setOnClickListenerWithViber {
            binding.playPause.startAnimation(outAnim)
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        mediaPlayer.release()
        super.onDestroy()
    }


    private fun filledTrackMeans(track: Track) {
        binding.addFavorite.isClickable = true
        binding.addCollection.isClickable = true
        binding.timerMedia.text = SimpleDateFormat(
            "mm:ss", Locale.getDefault()
        ).format(timerStart)
        Glide.with(this).load(track.getCoverArtwork())
            .placeholder(R.drawable.placeholder_media_image).centerCrop()
            .transform(RoundedCorners(roundedCorners)).into(binding.trackImageMedia)
        binding.trackNameMedia.text = track.trackName
        binding.trackArtistMedia.text = track.artistName
        binding.yearMediaMean.text = track.getYear()
        binding.countryMediaMean.text = track.country
        binding.genreMediaMean.text = track.primaryGenreName
        binding.timeMediaMean.text = track.getTrackTime()
        if (track.collectionName != null) {
            binding.albumMediaMean.isVisible = true
            binding.albumMedia.isVisible = true
            binding.albumMediaMean.text = track.collectionName
        } else {
            binding.albumMediaMean.isVisible = false
            binding.albumMedia.isVisible = false
        }
    }

    private fun preparePlayer() {
        // передаем ссылку на 30 сек отрывок медиа плееру
        mediaPlayer.setDataSource(trackUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.playPause.isEnabled = true
            playerState = PlayerState.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            binding.playPause.setImageDrawable(getDrawable(R.drawable.play_button))
            binding.timerMedia.text = SimpleDateFormat(
                "mm:ss", Locale.getDefault()
            ).format(timerStart)
            playerState = PlayerState.STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.playPause.setImageDrawable(getDrawable(R.drawable.pause_button))
        playerState = PlayerState.STATE_PLAYING
        handlerMain?.post(updateTimerMedia())
    }

    private fun pausePlayer() {
        handlerMain?.removeCallbacks(updateTimerMedia())
        mediaPlayer.pause()
        binding.playPause.setImageDrawable(getDrawable(R.drawable.play_button))
        playerState = PlayerState.STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
            }

            else -> {}
        }
    }

    private fun updateTimerMedia(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == PlayerState.STATE_PLAYING) {
                    binding.timerMedia.text = SimpleDateFormat(
                        "mm:ss", Locale.getDefault()
                    ).format(mediaPlayer.currentPosition)
                    timerStop = mediaPlayer.currentPosition.toLong()
                    handlerMain?.postDelayed(this, UPDATE_TIMER_TRACK)
                }
            }

        }
    }
}
