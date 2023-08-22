package com.example.playlistmaker.ui.Find

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.setOnClickListenerWithViber


class FindAdapter(val trackClickListner: TrackClickListner) :
    RecyclerView.Adapter<FindViewHolder>() {
    var trackList = ArrayList<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return FindViewHolder(view)
    }

    override fun getItemCount() = trackList.size

    override fun onBindViewHolder(holder: FindViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListenerWithViber {
            trackClickListner.onTrackClick(trackList.get(position))
        }
    }
    fun interface TrackClickListner {
        fun onTrackClick(track: Track)
    }
}
