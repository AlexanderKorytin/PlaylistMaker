package com.example.playlistmaker.playlist.ui

import android.os.Environment
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.app.dpToPx
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.playlist.domain.models.PlayList
import java.io.File

class PlayListViewHolder(private val parentViewBinding: PlaylistItemBinding) :
    RecyclerView.ViewHolder(parentViewBinding.root) {
    private val radiusIconTrackDp = 8.0f
    private val radiusIconTrackPx = dpToPx(radiusIconTrackDp, parentViewBinding.root.context)
    fun bind(playList: PlayList) {
        val filePath = File(
            itemView.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "playlist_maker_album"
        )
        val file = File(filePath, playList.playListCover)

        parentViewBinding.quantityTracks.text =
            "${playList.quantityTracks} ${
                itemView.context.resources.getQuantityString(
                    R.plurals.tracks_plurals,
                    playList.quantityTracks
                )
            }"
        parentViewBinding.playlistNameRecycler.text = playList.playListName
        Glide
            .with(parentViewBinding.root.context)
            .load(file.toUri())
            .placeholder(R.drawable.placeholder_playlist_item)
            .transform(CenterCrop(), RoundedCorners(radiusIconTrackPx))
            .into(parentViewBinding.listCoverRecycler)

    }
}