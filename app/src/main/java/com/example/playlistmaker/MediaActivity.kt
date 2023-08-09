package com.example.playlistmaker

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
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
    val trackNullMean = "-"

    // инициализируем медиа плеер
    // и задаем его состояние по умолчанию
    private var mediaPlayer = MediaPlayer()
    var playerState = PlayerState.STATE_DEFAULT
    private lateinit var trackUrl: String

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
        if (clickedTrack != null) {
            filledTrackMeans(clickedTrack)
            trackUrl = clickedTrack.previewUrl
        } else filledNullTrackMeans()

        // Подготовка плеера и установка слушателей
        preparePlayer()

        binding.backMedia.setOnClickListenerWithViber {
            finish()
        }

        binding.playPause.setOnClickListener {
            playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }


    private fun filledTrackMeans(track: Track) {
        binding.addFavorite.isClickable = true
        binding.addCollection.isClickable = true
        Glide
            .with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.placeholder_media_image)
            .centerCrop()
            .transform(RoundedCorners(roundedCorners))
            .into(binding.trackImageMedia)
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

    private fun filledNullTrackMeans() {
        binding.albumMediaMean.isVisible = true
        binding.albumMedia.isVisible = true
        binding.playPause.isClickable = false
        binding.addFavorite.isClickable = false
        binding.addCollection.isClickable = false
        Glide
            .with(this)
            .load(R.drawable.placeholder_media_image)
            .centerCrop()
            .transform(RoundedCorners(roundedCorners))
            .into(binding.trackImageMedia)
        binding.trackNameMedia.text = trackNullMean
        binding.trackArtistMedia.text = trackNullMean
        binding.yearMediaMean.text = trackNullMean
        binding.countryMediaMean.text = trackNullMean
        binding.genreMediaMean.text = trackNullMean
        binding.timeMediaMean.text = trackNullMean
        binding.albumMediaMean.text = trackNullMean

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
            playerState = PlayerState.STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.playPause.setImageDrawable(getDrawable(R.drawable.pause_button))
        playerState = PlayerState.STATE_PLAYING
        handlerMain?.post ( updateTimerMedia() )
    }

    private fun pausePlayer() {
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
        return object :Runnable {
            override fun run() {
                binding.timerMedia.text = SimpleDateFormat(
                    "mm:ss", Locale
                        .getDefault()
                )
                    .format(mediaPlayer.currentPosition)
                handlerMain?.postDelayed(this, UPDATE_TIMER_TRACK)
            }

        }
    }
}
