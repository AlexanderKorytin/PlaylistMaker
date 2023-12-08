package com.example.playlistmaker.player.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.playlist.domain.models.PlayList

class PlaylistsDiffUtilPlayer: DiffUtil.ItemCallback<PlayList>() {
    override fun areItemsTheSame(oldItem: PlayList, newItem: PlayList): Boolean =
        oldItem.playListId == newItem.playListId


    override fun areContentsTheSame(oldItem: PlayList, newItem: PlayList): Boolean {
        return oldItem.playListId == newItem.playListId &&
                oldItem.playListCover == newItem.playListCover &&
                oldItem.playListName == newItem.playListName &&
                oldItem.playListDescription == newItem.playListDescription &&
                oldItem.tracksIds == newItem.tracksIds &&
                oldItem.quantityTracks == newItem.quantityTracks
    }
}