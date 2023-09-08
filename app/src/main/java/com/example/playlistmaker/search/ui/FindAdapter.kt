package com.example.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Util.setOnClickListenerWithViber
import com.example.playlistmaker.databinding.TrackViewBinding
import com.example.playlistmaker.search.ui.models.TrackUI


class FindAdapter(val trackClickListner: TrackClickListner) :
    RecyclerView.Adapter<FindViewHolder>() {
    var trackList = ArrayList<TrackUI>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        val viewBinding = TrackViewBinding.inflate(layoutInspector, parent, false)
        return FindViewHolder(viewBinding)
    }

    override fun getItemCount() = trackList.size

    override fun onBindViewHolder(holder: FindViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListenerWithViber {
            trackClickListner.onTrackClick(trackList.get(position))
        }
    }

    fun interface TrackClickListner {
        fun onTrackClick(track: TrackUI)
    }
}
