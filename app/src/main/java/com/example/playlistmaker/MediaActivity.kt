package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.google.gson.Gson


class MediaActivity : AppCompatActivity() {

    companion object {
        private const val CLICKED_TRACK = "CLICKED_TRACK"
        private const val cornersRatio = 120
    }

    private var receivedTrack:String? = null
    private var widthDisplay = 0
    private var roundedCorners = 0
    private lateinit var binding: ActivityMediaBinding
    val trackNullMean = "-"

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CLICKED_TRACK, receivedTrack)
    }
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
        receivedTrack = intent.getStringExtra("clickedTrack")
        val clickedTrack = Gson().fromJson(receivedTrack, Track::class.java)
        if (clickedTrack != null) filledTrackMeans(clickedTrack) else filledNullTrackMeans()

        binding.backMedia.setOnClickListenerWithViber {
            finish()
        }
    }


    private fun filledTrackMeans(track: Track) {
        binding.playPause.isClickable = true
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
}
