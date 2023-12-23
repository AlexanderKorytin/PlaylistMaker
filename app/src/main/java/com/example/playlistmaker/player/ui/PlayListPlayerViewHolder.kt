package com.example.playlistmaker.player.ui

import android.os.Environment
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistItemBottomSheetBinding
import com.example.playlistmaker.playlist.domain.models.PlayList
import java.io.File

class PlayListPlayerViewHolder(private val parentBinding: PlaylistItemBottomSheetBinding) :
    RecyclerView.ViewHolder(parentBinding.root) {

    fun bind(playList: PlayList) {
        val filePath = File(
            itemView.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "playlist_maker_album"
        )
        val file = File(filePath, playList.playListCover)

        parentBinding.playlistQuantityTracksBottomSheet.text = "${playList.quantityTracks} ${
            itemView.context.resources.getQuantityString(
                R.plurals.tracks_plurals,
                playList.quantityTracks
            )
        }"
        parentBinding.playlistNameBottomSheet.text = playList.playListName
        Glide
            .with(parentBinding.root.context)
            .load(file.toUri())
            .placeholder(R.drawable.placeholder_media_image)
            .transform(CenterCrop())
            .into(parentBinding.playlistCoverBottomSheet)

    }
}