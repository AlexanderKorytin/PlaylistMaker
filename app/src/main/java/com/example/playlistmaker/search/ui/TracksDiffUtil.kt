package com.example.playlistmaker.search.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.search.ui.models.TrackUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TracksDiffUtil(
    private val oldList: ArrayList<TrackUI>,
    private val newList: ArrayList<TrackUI>
) : DiffUtil.Callback() {


    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].trackId == newList[newItemPosition].trackId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val newTrack = newList[newItemPosition]
        val oldTrack = oldList[oldItemPosition]
        return compare(oldTrack, newTrack)
    }

    private fun compare(oldTrack: TrackUI, newTrack: TrackUI): Boolean {

        return oldTrack.trackId == newTrack.trackId &&
                oldTrack.trackName == newTrack.trackName &&
                oldTrack.artistName == newTrack.artistName &&
                oldTrack.trackTime == newTrack.trackTime &&
                oldTrack.inFavorite == newTrack.inFavorite &&
                oldTrack.artworkUrl100 == newTrack.artworkUrl100 &&
                oldTrack.collectionName == newTrack.collectionName &&
                oldTrack.country == newTrack.country &&
                oldTrack.coverArtWork == newTrack.coverArtWork &&
                oldTrack.primaryGenreName == newTrack.primaryGenreName

    }


}

suspend fun updateTracksList(adapter: FindAdapter, newList: ArrayList<TrackUI>) {
    val tracksDiffUtilCallBack = TracksDiffUtil(adapter.trackList, newList)
    val tracksDiffResult = DiffUtil.calculateDiff(tracksDiffUtilCallBack, true)
    adapter.trackList = newList
    withContext(Dispatchers.Main) {
        tracksDiffResult.dispatchUpdatesTo(adapter)
    }
}