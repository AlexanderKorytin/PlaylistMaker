package com.example.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.app.dpToPx
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.ui.models.ClickedTrackGson
import com.example.playlistmaker.player.ui.viewmodel.MediaPlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class MediaActivity : AppCompatActivity(R.layout.activity_media) {


    private var receivedTrack: String? = null
    private lateinit var binding: ActivityMediaBinding
    private lateinit var outAnim: Animation
    private lateinit var inAnim: Animation

    private val playerVM: MediaPlayerViewModel by viewModel<MediaPlayerViewModel> {
        parametersOf(ClickedTrackGson(intent.getStringExtra("clickedTrack")))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val radiusIconTrackDp = 8.0f
        val radiusIconTrackPx = dpToPx(radiusIconTrackDp, this)

        receivedTrack = savedInstanceState?.getString(CLICKED_TRACK, "")
            ?: intent.getStringExtra("clickedTrack")
        playerVM.getPlayerScreenState().observe(this) { currentPlayerState ->
            when (currentPlayerState.playerState) {
                PlayerState.STATE_DEFAULT -> {
                    with(binding) {
                        playPause.isEnabled = false
                        timerMedia.text = currentPlayerState.currentTime
                    }
                }

                PlayerState.STATE_PREPARED -> {
                    with(binding) {
                        playPause.isEnabled = true
                        playPause.setImageDrawable(getDrawable(R.drawable.play_button))
                        timerMedia.text = currentPlayerState.currentTime
                    }
                }

                PlayerState.STATE_PLAYING -> {
                    with(binding) {
                        playPause.setImageDrawable(getDrawable(R.drawable.pause_button))
                        timerMedia.text = currentPlayerState.currentTime
                    }

                }

                else -> {
                    with(binding) {
                        timerMedia.text = currentPlayerState.currentTime
                        playPause.setImageDrawable(getDrawable(R.drawable.play_button))
                    }
                }

            }
        }

        playerVM.getCurrentTrack().observe(this) { track ->

            if (track.previewUrl == "") {
                binding.playPause.isEnabled = false
                Toast.makeText(this, getString(R.string.No_media_for_playing), Toast.LENGTH_LONG)
                    .show()
            } else binding.playPause.isEnabled = true

            Glide
                .with(this)
                .load(track.coverArtWork)
                .placeholder(R.drawable.placeholder_media_image)
                .transform(CenterCrop(), RoundedCorners(radiusIconTrackPx))
                .into(binding.trackImageMedia)
            with(binding) {
                addFavorite.isClickable = true
                addCollection.isClickable = true
                trackNameMedia.text = track.trackName
                trackArtistMedia.text = track.artistName
                yearMediaMean.text = track.year
                countryMediaMean.text = track.country
                genreMediaMean.text = track.primaryGenreName
                timeMediaMean.text = track.trackTime
            }

            if (track.collectionName != "") {
                with(binding) {
                    albumMediaMean.isVisible = true
                    albumMedia.isVisible = true
                    albumMediaMean.text = track.collectionName
                }
            } else {
                with(binding) {
                    albumMediaMean.isVisible = false
                    albumMedia.isVisible = false
                }

            }
        }

        binding.addFavorite.setOnClickListener {
            playerVM.changedSingInFavorite()
        }

        playerVM.getCurrentSingFavorite().observe(this) { sing ->
            if (sing) binding.addFavorite.setImageDrawable(getDrawable(R.drawable.in_favorite_true))
            else binding.addFavorite.setImageDrawable(getDrawable(R.drawable.in_favorite_false))
        }

        //загружаем анимации
        inAnim = AnimationUtils.loadAnimation(this, R.anim.fadein)
        outAnim = AnimationUtils.loadAnimation(this, R.anim.fadeout)
        //добавляем слушателя на анимацию
        outAnim.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {

            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })
        inAnim.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                playerVM.playbackControl()
            }

            override fun onAnimationEnd(animation: Animation?) {
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })
        binding.backMedia.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }

        binding.playPause.setOnTouchListener { v, event ->
            when (event.action) {

                MotionEvent.ACTION_DOWN -> {
                    v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_PRESS)
                    binding.playPause.startAnimation(outAnim)
                    return@setOnTouchListener false
                }

                MotionEvent.ACTION_UP -> {
                    v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_PRESS)
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

    companion object {
        private const val CLICKED_TRACK = "CLICKED_TRACK"
        private const val CURRENTTRACK = "clickedTrack"
        fun createArgs(clickedTrack: String?): Bundle = bundleOf(CURRENTTRACK to clickedTrack)
    }
}
