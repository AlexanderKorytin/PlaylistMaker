package com.example.playlistmaker.presentation.ui.find

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackViewBinding
import com.example.playlistmaker.dpToPx
import com.example.playlistmaker.presentation.models.TrackUI

class FindViewHolder(private val parentViewBinding: TrackViewBinding) :
    RecyclerView.ViewHolder(parentViewBinding.root) {

    private val trackIcon: ImageView = parentViewBinding.trackIcon
    private val trackName: TextView = parentViewBinding.trackName
    private val artistName: TextView = parentViewBinding.artistName
    private val trackTime: TextView = parentViewBinding.trackTime
    private val radiusIconTrackDp = 2.0f
    private val radiusIconTrackPx = dpToPx(radiusIconTrackDp, parentViewBinding.root.context)
    private val imageLoaderGlide = App().creator.provideGetImageLoaderUseCase(trackIcon)

    fun bind(track: TrackUI) {

        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = track.trackTime

        imageLoaderGlide.execute(
            track.artworkUrl100,
            R.drawable.placeholdersnake,
            radiusIconTrackPx
        )

    }
}