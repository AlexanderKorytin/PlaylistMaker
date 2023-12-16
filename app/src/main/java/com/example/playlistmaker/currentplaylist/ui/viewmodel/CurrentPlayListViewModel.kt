package com.example.playlistmaker.currentplaylist.ui.viewmodel

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.currentplaylist.domain.api.CurrentPlayListInteractor
import com.example.playlistmaker.currentplaylist.ui.models.CurrentPlayListScreenState
import com.example.playlistmaker.currentplaylist.ui.models.PlayListUI
import com.example.playlistmaker.playlist.domain.models.PlayList
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.mappers.MapToTrackUI
import com.example.playlistmaker.util.getEndMessage
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class CurrentPlayListViewModel(
    val playListId: Int,
    private val currentPlayListInteractor: CurrentPlayListInteractor,
    val mapper: MapToTrackUI,
    val json: Gson
) : ViewModel() {


    private val playlist: MutableLiveData<PlayListUI> = MutableLiveData()

    fun getPlayList(): LiveData<PlayListUI> = playlist

    private val currentPlayListScreenState: MutableLiveData<CurrentPlayListScreenState> =
        MutableLiveData()

    fun getCurrentScreenState(): LiveData<CurrentPlayListScreenState> = currentPlayListScreenState

    fun getCurrentPlayListById() {
        viewModelScope.launch(Dispatchers.IO) {
            val album = currentPlayListInteractor.getPlayListById(playListId)
            getTracks(album.tracksIds)
            val albumUI = PlayListUI(
                playListName = album.playListName,
                playListDescription = album.playListDescription,
                counterTrack = getCounter(album),
                playListCover = album.playListCover
            )
            playlist.postValue(albumUI)
        }
    }

    suspend fun getTracks(tracksId: List<Long>) {
        currentPlayListInteractor.getTracksFromPlailist(tracksId)
            .collect { result ->
                when {
                    result.isEmpty() -> {
                        currentPlayListScreenState.postValue(
                            CurrentPlayListScreenState.Empty
                        )
                    }

                    else -> {
                        val time = getSummaryTime(result)
                        currentPlayListScreenState.postValue(
                            CurrentPlayListScreenState.Content(result, time)
                        )
                    }
                }
            }
    }

    private suspend fun getSummaryTime(tracks: List<Track>): String {
        var timeTracks = 0L
        for (track in tracks) {
            val df = SimpleDateFormat("hh:mm")
            timeTracks += df.parse(track.trackTime).time
        }
        val minutes = SimpleDateFormat("hh", Locale.getDefault()).format(timeTracks)
        val end = getEndMessageForTime(minutes.toInt())
        return "${minutes} ${end}"
    }

    private suspend fun getCounter(playList: PlayList): String {
        return "${playList.quantityTracks} ${getEndMessage(playList.quantityTracks)}"
    }

    private fun getEndMessageForTime(time: Int): String {
        val endMessage = when (time % 100) {
            in 11..19 -> "минут"
            else -> {
                when (time % 10) {
                    1 -> "минута"
                    in 2..4 -> "минуты"
                    else -> "минут"
                }
            }
        }
        return endMessage
    }
}