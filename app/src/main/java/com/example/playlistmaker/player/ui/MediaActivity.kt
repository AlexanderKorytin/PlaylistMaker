package com.example.playlistmaker.presentation.ui.mediaPlayer

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.Util.App
import com.example.playlistmaker.Util.dpToPx
import com.example.playlistmaker.Util.setOnClickListenerWithViber
import com.example.playlistmaker.Util.setVibe
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.ui.mappers.GetClickedTrackFromGsonUseCase
import com.example.playlistmaker.player.ui.models.ClickedTrackGson
import com.example.playlistmaker.player.ui.viewmodel.MediaPlayerViewModel
import com.example.playlistmaker.player.ui.viewmodel.MediaPlayerViewModelFactory


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
    private val getClickedTrack = GetClickedTrackFromGsonUseCase()
    private lateinit var playerVM: MediaPlayerViewModel

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
        val clickedTrack = getClickedTrack.map(ClickedTrackGson(receivedTrack))
        App().creator.url = clickedTrack.toMediaPlayer()

        playerVM = ViewModelProvider(
            this,
            MediaPlayerViewModelFactory(clickedTrack, binding.trackImageMedia, roundedCorners)
        ).get(MediaPlayerViewModel::class.java)


        playerVM.getPlayerScreenState().observe(this) { currentPlayerState ->
            when (currentPlayerState.playerState) {
                PlayerState.STATE_PREPARED, PlayerState.STATE_DEFAULT -> {
                    binding.playPause.setImageDrawable(getDrawable(R.drawable.play_button))
                    binding.timerMedia.text = currentPlayerState.currentTime
                }

                PlayerState.STATE_PLAYING -> {
                    binding.playPause.setImageDrawable(getDrawable(R.drawable.pause_button))
                    binding.timerMedia.text = currentPlayerState.currentTime
                }

                else -> {
                    binding.timerMedia.text = currentPlayerState.currentTime
                    binding.playPause.setImageDrawable(getDrawable(R.drawable.play_button))
                }

            }
        }

        playerVM.getCurrentTrack().observe(this) { track ->

            binding.addFavorite.isClickable = true
            binding.addCollection.isClickable = true
            Glide
                .with(this)
                .load(track.coverArtWork)
                .placeholder(R.drawable.placeholder_media_image)
                .centerCrop()
                .transform(RoundedCorners(roundedCorners))
                .into(binding.trackImageMedia)
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
                playerVM.playbackControl()
            }

            override fun onAnimationEnd(animation: Animation?) {
                setVibe()
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })
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
        playerVM.pausePlayer()
    }

//    private fun filledTrackMeans(track: ClickedTrack) {
//        binding.addFavorite.isClickable = true
//        binding.addCollection.isClickable = true
//        imageLoaderUseCase
//            .execute(
//                track.coverArtWork,
//                R.drawable.placeholder_media_image,
//                binding.trackImageMedia,
//                roundedCorners
//            )
//        binding.trackNameMedia.text = track.trackName
//        binding.trackArtistMedia.text = track.artistName
//        binding.yearMediaMean.text = track.year
//        binding.countryMediaMean.text = track.country
//        binding.genreMediaMean.text = track.primaryGenreName
//        binding.timeMediaMean.text = track.trackTime
//        if (track.collectionName != "") {
//            binding.albumMediaMean.isVisible = true
//            binding.albumMedia.isVisible = true
//            binding.albumMediaMean.text = track.collectionName
//        } else {
//            binding.albumMediaMean.isVisible = false
//            binding.albumMedia.isVisible = false
//        }
//    }
}
