package com.example.playlistmaker

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class FindViewHolder(private val parentView: View) : RecyclerView.ViewHolder(parentView) {

    private val trackIcon: ImageView = parentView.findViewById(R.id.trackIcon)
    private val trackName: TextView = parentView.findViewById(R.id.trackName)
    private val artistName: TextView = parentView.findViewById(R.id.artistName)
    private val trackTime: TextView = parentView.findViewById(R.id.trackTime)
    private val radiusIconTrack = dpToPx(2f, parentView.context)

    fun bind(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = track.trackTime
        Glide
            .with(parentView.context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholdersnake)
            .centerCrop()
            .transform(RoundedCorners(radiusIconTrack))
            .into(trackIcon)
    }
    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
}