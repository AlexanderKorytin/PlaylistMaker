package com.example.playlistmaker.presentation.ui.find

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.data.dto.ImageLoaderGlide
import com.example.playlistmaker.domain.impl.ImageLoaderUseCase
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.setOnClickListenerWithViber


class FindAdapter(val trackClickListner: TrackClickListner) :
    RecyclerView.Adapter<FindViewHolder>() {
    var trackList = ArrayList<Track>()
    val imageLoader = ImageLoaderGlide()
    val imageLoaderUseCase = ImageLoaderUseCase()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return FindViewHolder(view, imageLoaderUseCase)
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
