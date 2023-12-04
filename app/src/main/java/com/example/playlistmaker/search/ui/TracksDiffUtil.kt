package com.example.playlistmaker.search.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.search.ui.models.TrackUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TracksDiffUtil(
) : DiffUtil.ItemCallback<TrackUI>() {

    override fun areItemsTheSame(oldItem: TrackUI, newItem: TrackUI): Boolean {
       return oldItem.trackId == newItem.trackId
    }

    override fun areContentsTheSame(oldItem: TrackUI, newItem: TrackUI): Boolean {
        return oldItem.trackId == newItem.trackId &&
                oldItem.trackName == newItem.trackName &&
                oldItem.artistName == newItem.artistName &&
                oldItem.trackTime == newItem.trackTime &&
                oldItem.inFavorite == newItem.inFavorite &&
                oldItem.artworkUrl100 == newItem.artworkUrl100 &&
                oldItem.collectionName == newItem.collectionName &&
                oldItem.country == newItem.country &&
                oldItem.coverArtWork == newItem.coverArtWork &&
                oldItem.primaryGenreName == newItem.primaryGenreName
    }


}
