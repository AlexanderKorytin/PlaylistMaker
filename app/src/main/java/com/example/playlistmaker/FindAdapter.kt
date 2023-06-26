package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class FindAdapter(val trackList: ArrayList<Track>) : RecyclerView.Adapter<FindViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return FindViewHolder(view)
    }

    override fun getItemCount() = trackList.size

    override fun onBindViewHolder(holder: FindViewHolder, position: Int) {
        holder.bind(trackList[position])
    }
}
