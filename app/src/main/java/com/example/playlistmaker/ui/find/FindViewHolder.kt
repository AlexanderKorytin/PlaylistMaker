package com.example.playlistmaker.ui.find

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.data.dto.ImageLoaderGlide
import com.example.playlistmaker.domain.api.ImageLoader
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentetion.dpToPx

class FindViewHolder(private val parentView: View, val imageLoaderGlide: ImageLoader) : RecyclerView.ViewHolder(parentView) {

    private val trackIcon: ImageView = parentView.findViewById(R.id.trackIcon)
    private val trackName: TextView = parentView.findViewById(R.id.trackName)
    private val artistName: TextView = parentView.findViewById(R.id.artistName)
    private val trackTime: TextView = parentView.findViewById(R.id.trackTime)
    private val radiusIconTrackDp = 2.0f
    private val radiusIconTrackPx = dpToPx(radiusIconTrackDp, parentView.context)

    fun bind(track: Track) {

        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = track.trackTime

        imageLoaderGlide.loadImage(
            track.artworkUrl100,
            R.drawable.placeholdersnake,
            trackIcon,
            radiusIconTrackPx
        )

    }
}