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
            "${playList.quantityTracks} ${getEndMessage(playList.quantityTracks)}"
        parentViewBinding.playlistNameRecycler.text = playList.playListName
        Glide
            .with(parentViewBinding.root.context)
            .load(file.toUri())
            .placeholder(R.drawable.placeholder_media_image)
            .transform(CenterCrop(), RoundedCorners(radiusIconTrackPx))
            .into(parentViewBinding.listCoverRecycler)

    }

    private fun getEndMessage(count: Int): String {
        val endMessage = when (count % 100) {
            in 11..19 -> "треков"
            else -> {
                when (count % 10) {
                    1 -> "трек"
                    in 2..4 -> "трека"
                    else -> "треков"
                }
            }
        }
        return endMessage
    }
}