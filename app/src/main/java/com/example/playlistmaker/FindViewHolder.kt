package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class FindViewHolder(val parentView: View) : RecyclerView.ViewHolder(parentView) {

    val trackIcon: ImageView
    val trackName: TextView
    val artistName: TextView
    val trackTime: TextView

    init {
        trackIcon = parentView.findViewById(R.id.trackIcon)
        trackName = parentView.findViewById(R.id.trackName)
        artistName = parentView.findViewById(R.id.artistName)
        trackTime = parentView.findViewById(R.id.trackTime)
    }

    fun bind(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = track.trackTime
        Glide
            .with(parentView.context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.place_holder)
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(trackIcon)
    }
}