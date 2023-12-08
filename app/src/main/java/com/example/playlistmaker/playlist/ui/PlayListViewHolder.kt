package com.example.playlistmaker.playlist.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.app.dpToPx
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.playlist.domain.models.PlayList

class PlayListViewHolder(private val parentViewBinding: PlaylistItemBinding) :
    RecyclerView.ViewHolder(parentViewBinding.root) {
    private val radiusIconTrackDp = 8.0f
    private val radiusIconTrackPx = dpToPx(radiusIconTrackDp, parentViewBinding.root.context)
    fun bind(playList: PlayList) {

        parentViewBinding.quantityTracks.text = playList.quantityTracks.toString()
        parentViewBinding.playlistNameRecycler.text = playList.playListName
        Glide
            .with(parentViewBinding.root.context)
            .load(playList.playListCover)
            .placeholder(R.drawable.placeholder_media_image)
            .transform(CenterCrop(), RoundedCorners(radiusIconTrackPx))
            .into(parentViewBinding.listCoverRecycler)

    }
}