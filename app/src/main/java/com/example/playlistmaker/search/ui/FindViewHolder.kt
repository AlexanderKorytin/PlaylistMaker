package com.example.playlistmaker.search.ui

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackViewBinding
import com.example.playlistmaker.Util.dpToPx
import com.example.playlistmaker.search.ui.models.TrackUI

class FindViewHolder(private val parentViewBinding: TrackViewBinding) :
    RecyclerView.ViewHolder(parentViewBinding.root) {

    private val trackIcon: ImageView = parentViewBinding.trackIcon
    private val trackName: TextView = parentViewBinding.trackName
    private val artistName: TextView = parentViewBinding.artistName
    private val trackTime: TextView = parentViewBinding.trackTime
    private val radiusIconTrackDp = 2.0f
    private val radiusIconTrackPx = dpToPx(radiusIconTrackDp, parentViewBinding.root.context)

    fun bind(track: TrackUI) {

        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = track.trackTime

        Glide
            .with(parentViewBinding.root.context)
            .load(track.coverArtWork)
            .placeholder(R.drawable.placeholder_media_image)
            .centerCrop()
            .transform(RoundedCorners(radiusIconTrackPx))
            .into(parentViewBinding.trackIcon)

    }
}