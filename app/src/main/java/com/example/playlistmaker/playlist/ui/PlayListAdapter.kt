package com.example.playlistmaker.playlist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.playlist.domain.models.PlayList

class PlayListAdapter(private val CurrentPlayListClixk: OnPlayListClick) :
    ListAdapter<PlayList, PlayListViewHolder>(PlayListsDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        val viewBinding =
            PlaylistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayListViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        holder.bind(currentList[position])
        holder.itemView.setOnClickListener {
            CurrentPlayListClixk.onClick(currentList[holder.adapterPosition])
        }
    }

    fun interface OnPlayListClick {
        fun onClick(playList: PlayList)
    }
}