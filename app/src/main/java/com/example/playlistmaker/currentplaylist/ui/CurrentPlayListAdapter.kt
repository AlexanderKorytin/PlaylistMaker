package com.example.playlistmaker.currentplaylist.ui

import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.databinding.TrackViewBinding
import com.example.playlistmaker.search.ui.FindViewHolder
import com.example.playlistmaker.search.ui.TracksDiffUtil
import com.example.playlistmaker.search.ui.models.TrackUI


class CurrentPlayListAdapter(
    private val trackClickListner: TrackClickListner,
    private val trackLongClickLisner: TrackLongClickListner
) :
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
            trackClickListner.onTrackClick(currentList[holder.adapterPosition])
        }

        holder.itemView.setOnLongClickListener {
            trackLongClickLisner.onTrackLongClick(currentList[position])
            return@setOnLongClickListener false
        }
    }

    fun interface TrackClickListner {
        fun onTrackClick(trackUI: TrackUI)
    }

    fun interface TrackLongClickListner {
        fun onTrackLongClick(trackUI: TrackUI)
    }
}
