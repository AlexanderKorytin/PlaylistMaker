package com.example.playlistmaker.search.ui

import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.databinding.TrackViewBinding
import com.example.playlistmaker.search.ui.models.TrackUI


class FindAdapter(private val trackClickListner: TrackClickListner) :
    ListAdapter<TrackUI, FindViewHolder>(TracksDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        val viewBinding = TrackViewBinding.inflate(layoutInspector, parent, false)
        return FindViewHolder(viewBinding)
    }

    override fun getItemCount() = currentList.size

    override fun onBindViewHolder(holder: FindViewHolder, position: Int) {
        holder.bind(currentList[position])
        holder.itemView.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
            trackClickListner.onTrackClick(getItem(position))
        }
    }

    fun interface TrackClickListner {
        fun onTrackClick(track: TrackUI)
    }
}
