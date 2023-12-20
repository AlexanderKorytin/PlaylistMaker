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
    private val playListId: Int,
    private val currentPlayListInteractor: CurrentPlayListInteractor,
    val mapper: MapToTrackUI,
    val json: Gson
) : ViewModel() {

    private lateinit var album: PlayList

    private lateinit var trackList: List<Track>
    private lateinit var timeTracks: String

    fun getPlayList(): PlayList {
        return album
    }

    fun getPlayListId(): Int {
        return playListId
    }


    private val playlistScreenState: MutableLiveData<PlayListUI> = MutableLiveData()

    fun getPlayListImmutableState(): LiveData<PlayListUI> = playlistScreenState

    private val currentPlayListScreenState: MutableLiveData<CurrentPlayListScreenState> =
        MutableLiveData()

    fun getPlayListMutableState(): LiveData<CurrentPlayListScreenState> = currentPlayListScreenState

    fun getCurrentPlayListById() {
        viewModelScope.launch(Dispatchers.IO) {
            album = currentPlayListInteractor.getPlayListById(playListId)
            getTracks(album.tracksIds)
            val albumUI = PlayListUI(
                playListName = album.playListName,
                playListDescription = album.playListDescription,
                counterTrack = getCounter(album),
                playListCover = album.playListCover
            )
            playlistScreenState.postValue(albumUI)
        }
    }

    suspend fun getTracks(tracksIds: List<Long>) {
        currentPlayListInteractor.getTracksFromPlailist(tracksIds)
            .collect { result ->
                when {
                    result.isEmpty() -> {
                        currentPlayListScreenState.postValue(
                            CurrentPlayListScreenState.Empty(getSummaryTime(result))
                        )
                    }

                    else -> {

                        val resultUI = mutableListOf<Track>()
                        resultUI.addAll(result)
                        for (i in 0 until result.size) {
                            val index = tracksIds.indexOf(result[i].trackId)
                            resultUI[index] = result[i]
                        }
                        resultUI.reverse()
                        trackList = resultUI
                        val time = getSummaryTime(resultUI)
                        timeTracks = time
                        currentPlayListScreenState.postValue(
                            CurrentPlayListScreenState.Content(resultUI, time)
                        )
                    }
                }
            }
    }

    fun sharingPlayList() {
        var message = ""
        message += "playList: ${album.playListName}\n"
        if (album.playListDescription.isNotEmpty()) message += "${album.playListDescription}\n"
        message += if (album.quantityTracks < 10) "quantity tracks: 0${album.quantityTracks}\n"
        else "quantity tracks: ${album.quantityTracks}\n"
        message += "tracks:\n"
        for (i in 0 until trackList.size) {
            message += "${i + 1}. ${trackList[i].artistName} - ${trackList[i].trackName} (${trackList[i].trackTime})\n"
        }
        currentPlayListInteractor.shareTrackList(message)
    }

    fun deleteTrack(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            album = currentPlayListInteractor.getPlayListById(playListId)
            currentPlayListInteractor.deleteTrackFromPlayList(track, album)
            getCurrentPlayListById()
        }
    }

    fun deletePlayList() {
        val playList = getPlayList()
        viewModelScope.launch(Dispatchers.IO) {
            currentPlayListInteractor.deletePlayList(playList)
        }
    }

    private fun getSummaryTime(tracks: List<Track>): String {
        var timeTracks = 0L
        for (track in tracks) {
            val df = SimpleDateFormat("mm:ss")
            timeTracks += df.parse(track.trackTime).time
        }
        val minutes = SimpleDateFormat("mm", Locale.getDefault()).format(timeTracks)
        val end = getEndMessageForTime(minutes.toInt())
        return "${minutes} ${end}"
    }

    private fun getCounter(playList: PlayList): String {
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