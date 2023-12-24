package com.example.playlistmaker.currentplaylist.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.currentplaylist.domain.api.CurrentPlayListInteractor
import com.example.playlistmaker.currentplaylist.ui.models.CurrentPlayListScreenState
import com.example.playlistmaker.currentplaylist.ui.models.PlayListUI
import com.example.playlistmaker.playlist.domain.models.PlayList
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.mappers.MapToTrackUI
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

class CurrentPlayListViewModel(
    private val playListId: Int,
    private val currentPlayListInteractor: CurrentPlayListInteractor,
    val mapper: MapToTrackUI,
    private val context: Context,
    val json: Gson
) : ViewModel() {

    private var _album: PlayList? = null
    private var _trackList: List<Track>? = null
    private var _timeTracks: String? = null

    private val album get() = _album!!

    fun getPlayList(): PlayList {
        return album
    }

    fun getPlayListId(): Int {
        return playListId
    }

    private var _sharingmessage: MutableStateFlow<String> = MutableStateFlow("")

    fun getSharingMessage(): SharedFlow<String> = _sharingmessage

    private val _playlistScreenState: MutableLiveData<PlayListUI> = MutableLiveData()

    fun getPlayListImmutableState(): LiveData<PlayListUI> = _playlistScreenState

    private val _currentPlayListScreenState: MutableLiveData<CurrentPlayListScreenState> =
        MutableLiveData()

    fun getPlayListMutableState(): LiveData<CurrentPlayListScreenState> =
        _currentPlayListScreenState

    fun getCurrentPlayListById() {
        viewModelScope.launch(Dispatchers.IO) {
            _album = currentPlayListInteractor.getPlayListById(playListId)
            getTracks(album.tracksIds)
            val albumUI = PlayListUI(
                playListName = album.playListName,
                playListDescription = album.playListDescription,
                counterTrack = getCounter(album),
                playListCover = album.playListCover
            )
            _playlistScreenState.postValue(albumUI)
        }
    }

    suspend fun getTracks(tracksIds: List<Long>) {
        currentPlayListInteractor.getTracksFromPlailist(tracksIds)
            .collect { result ->
                when {
                    result.isEmpty() -> {
                        _currentPlayListScreenState.postValue(
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
                        _trackList = resultUI
                        val time = getSummaryTime(resultUI)
                        _timeTracks = time
                        _currentPlayListScreenState.postValue(
                            CurrentPlayListScreenState.Content(resultUI, time)
                        )
                    }
                }
            }
    }

    suspend fun getSharingPlayListMessage() {
        _sharingmessage.emit(currentPlayListInteractor.getSharingMessage(playListId))
    }

    fun deleteTrack(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            _album = currentPlayListInteractor.getPlayListById(playListId)
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
        var timeTracks = Duration.ZERO
        var timeSec = Duration.ZERO
        for (track in tracks) {
            val str = track.trackTime.split(':')
            timeTracks += str[0].toInt().minutes
            timeSec += str[1].toInt().seconds
        }
        timeTracks += timeSec
        val timeSum = timeTracks.toInt(DurationUnit.MINUTES)
        val end = context.resources.getQuantityString(R.plurals.minutes_plurals, timeSum)
        return if (timeSum < 10) "0${timeSum} ${end}" else "${timeSum} ${end}"
    }

    private fun getCounter(playList: PlayList): String {
        return "${playList.quantityTracks} ${
            context.resources.getQuantityString(
                R.plurals.tracks_plurals,
                playList.quantityTracks
            )
        }"
    }
}