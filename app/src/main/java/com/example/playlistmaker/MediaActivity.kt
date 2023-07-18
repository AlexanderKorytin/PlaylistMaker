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
private const val cornersRatio = 120
class MediaActivity : AppCompatActivity() {
    var widthDisplay = 0
    var roundedCorners = 0
    lateinit var imageTrack: ImageView
    lateinit var nameTrack: TextView
    lateinit var artistName: TextView
    lateinit var timeTrack: TextView
    lateinit var albumTrack: TextView
    lateinit var yearTrack: TextView
    lateinit var countryTrack: TextView
    lateinit var genreTrack: TextView
    lateinit var albumName: TextView
    lateinit var playPauseButton: ImageButton
    lateinit var addCollectionButton: ImageButton
    lateinit var addFavouriteButton: ImageButton
    val trackNullMean = "-"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)
        val back = findViewById<ImageView>(R.id.backMedia)
        imageTrack = findViewById(R.id.track_image_media)
        nameTrack = findViewById(R.id.track_name_media)
        artistName = findViewById(R.id.track_artist_media)
        timeTrack = findViewById(R.id.time_media_mean)
        albumTrack = findViewById(R.id.album_media_mean)
        yearTrack = findViewById(R.id.year_media_mean)
        countryTrack = findViewById(R.id.country_media_mean)
        genreTrack = findViewById(R.id.genre_media_mean)
        albumName = findViewById(R.id.album_media)
        playPauseButton = findViewById(R.id.play_pause)
        addCollectionButton = findViewById(R.id.add_collection)
        addFavouriteButton = findViewById(R.id.add_favorite)
        // вычисляем радиус углов от реального размера картинки исходя из параметров верстки радиус 8
        // при высоте дисплея 832 из которых обложка - 312
        // коэффициент примерно - 1/120
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        widthDisplay = displayMetrics.widthPixels
        roundedCorners = (dpToPx(widthDisplay.toFloat(), this))/cornersRatio

        val mediaHistorySharedPreferences =
            getSharedPreferences(

                TRACK_HISTORY_SHAREDPREFERENCES,
                MODE_PRIVATE
            )

        val jsonTrackList = mediaHistorySharedPreferences.getString(SEARCH_HISTORY_TRACK_LIST, null)
        val mediaArray = getTrackList(jsonTrackList)
        if (mediaArray.size != 0) filledTrackMeans(mediaArray[0]) else filledNullTrackMeans()
        back.setOnClickListenerWithViber {
            finish()
        }
    }


    private fun filledTrackMeans(track: Track) {
        playPauseButton.isClickable = true
        addFavouriteButton.isClickable = true
        addCollectionButton.isClickable = true
        Glide
            .with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.placeholder_media_image)
            .centerCrop()
            .transform(RoundedCorners(roundedCorners))
            .into(imageTrack)
        nameTrack.text = track.trackName
        artistName.text = track.artistName
        yearTrack.text = track.getYear()
        countryTrack.text = track.country
        genreTrack.text = track.primaryGenreName
        timeTrack.text = track.getTrackTime()
        if (track.collectionName != null) {
            albumTrack.isVisible = true
            albumName.isVisible = true
            albumTrack.text = track.collectionName
        } else {
            albumTrack.isVisible = false
            albumName.isVisible = false
        }
    }

    private fun filledNullTrackMeans() {
        albumTrack.isVisible = true
        albumName.isVisible = true
        playPauseButton.isClickable = false
        addFavouriteButton.isClickable = false
        addCollectionButton.isClickable = false
        Glide
            .with(this)
            .load(R.drawable.placeholder_media_image)
            .centerCrop()
            .transform(RoundedCorners(roundedCorners))
            .into(imageTrack)
        nameTrack.text = trackNullMean
        artistName.text = trackNullMean
        yearTrack.text = trackNullMean
        countryTrack.text = trackNullMean
        genreTrack.text = trackNullMean
        timeTrack.text = trackNullMean
        albumTrack.text = trackNullMean
    }
}
