package com.example.playlistmaker.playlist.ui.viewmodel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.playlist.domain.models.PlayList
import com.example.playlistmaker.playlist.ui.PlayListViewHolder

class PlayListAdapter : ListAdapter<PlayList, PlayListViewHolder>(PlayListsDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        val viewBinding =
            PlaylistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayListViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

}