package com.example.playlistmaker.presentetion.ui.mediaPlayer

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.domain.impl.GetClickedTrackFromGsonUseCase
import com.example.playlistmaker.domain.impl.ImageLoaderUseCase
import com.example.playlistmaker.domain.models.ClickedTrack
import com.example.playlistmaker.domain.models.ClickedTrackGson
import com.example.playlistmaker.data.dto.PlayerState
import com.example.playlistmaker.data.mediaplayer.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.presentetion.dpToPx
import com.example.playlistmaker.presentetion.setOnClickListenerWithViber
import com.example.playlistmaker.presentetion.setVibe
import java.util.Locale


class MediaActivity : AppCompatActivity() {

    companion object {
        private const val CLICKED_TRACK = "CLICKED_TRACK"
        private const val cornersRatio = 120
    }

    private var receivedTrack: String? = null
    private var widthDisplay = 0
    private var roundedCorners = 0
    private lateinit var binding: ActivityMediaBinding
    private lateinit var outAnim: Animation
    private lateinit var inAnim: Animation

    // инициализируем медиа плеер
    // и задаем его состояние по умолчанию
    private var trackUrl: String? = null

    private val imageLoaderUseCase = ImageLoaderUseCase()
    private val getClickedTrack = GetClickedTrackFromGsonUseCase()
    private lateinit var mediaPlayer: MediaPlayerInteractorImpl

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CLICKED_TRACK, receivedTrack)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // вычисляем радиус углов от реального размера картинки исходя из параметров верстки радиус 8
        // при высоте дисплея 832 из которых обложка - 312
        // коэффициент примерно - 1/120
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        widthDisplay = displayMetrics.widthPixels
        roundedCorners = (dpToPx(widthDisplay.toFloat(), this)) / cornersRatio

        receivedTrack = savedInstanceState?.getString(CLICKED_TRACK, "")
            ?: intent.getStringExtra("clickedTrack")
        val clickedTrack = getClickedTrack.execute(ClickedTrackGson(receivedTrack))
        trackUrl = clickedTrack.previewUrl
        mediaPlayer = MediaPlayerInteractorImpl(clickedTrack, binding, this)
        filledTrackMeans(clickedTrack)


        //загружаем анимации
        inAnim = AnimationUtils.loadAnimation(this, R.anim.fadein)
        outAnim = AnimationUtils.loadAnimation(this, R.anim.fadeout)
        //добавляем слушателя на анимацию
        outAnim.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                setVibe()
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })
        inAnim.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                mediaPlayer.playbackControl()
            }

            override fun onAnimationEnd(animation: Animation?) {
                setVibe()
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })

// Подготовка плеера и установка слушателей
        binding.backMedia.setOnClickListenerWithViber {
            finish()
        }

        binding.playPause.setOnTouchListener { v, event ->
            when (event.action) {

                MotionEvent.ACTION_DOWN -> {
                    binding.playPause.startAnimation(outAnim)
                    return@setOnTouchListener false
                }

                MotionEvent.ACTION_UP -> {
                    binding.playPause.startAnimation(inAnim)
                    return@setOnTouchListener false
                }

                else -> {
                    return@setOnTouchListener true
                }
            }
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


    private fun filledTrackMeans(track: ClickedTrack) {
        binding.addFavorite.isClickable = true
        binding.addCollection.isClickable = true
        binding.timerMedia.text = SimpleDateFormat(
            "mm:ss", Locale.getDefault()
        ).format(mediaPlayer.getTimerStart())
        imageLoaderUseCase
            .execute(
                track.coverArtWork,
                R.drawable.placeholder_media_image,
                binding.trackImageMedia,
                roundedCorners
            )
        binding.trackNameMedia.text = track.trackName
        binding.trackArtistMedia.text = track.artistName
        binding.yearMediaMean.text = track.year
        binding.countryMediaMean.text = track.country
        binding.genreMediaMean.text = track.primaryGenreName
        binding.timeMediaMean.text = track.trackTime
        if (track.collectionName != "") {
            binding.albumMediaMean.isVisible = true
            binding.albumMedia.isVisible = true
            binding.albumMediaMean.text = track.collectionName
        } else {
            binding.albumMediaMean.isVisible = false
            binding.albumMedia.isVisible = false
        }
    }


    private fun startPlayer() {
        mediaPlayer.play()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
    }

}
