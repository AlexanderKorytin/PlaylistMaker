package com.example.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.databinding.PlaylistItemBottomSheetBinding
import com.example.playlistmaker.playlist.domain.models.PlayList

class PlayListsPlayerAdapter(private val playListClick: PlaylistClick) :
    ListAdapter<PlayList, PlayListPlayerViewHolder>(PlaylistsDiffUtilPlayer()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListPlayerViewHolder {
        val viewBinding = PlaylistItemBottomSheetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlayListPlayerViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: PlayListPlayerViewHolder, position: Int) {
        holder.bind(currentList[position])
        holder.itemView.setOnClickListener {
            playListClick.onClick(currentList[holder.adapterPosition])
        }
    }
    fun interface PlaylistClick{
        fun onClick(playList: PlayList)
    }
}