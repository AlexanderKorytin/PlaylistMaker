package com.example.playlistmaker.player.ui.viewmodel

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.favorite.domain.api.FavoriteTracksInteractor
import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.models.ClickedTrack
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.ui.mappers.MapClickedTrackGsonToClickedTrack
import com.example.playlistmaker.player.ui.models.ClickedTrackGson
import com.example.playlistmaker.player.ui.models.MediaPlayerScreenState
import com.example.playlistmaker.playlist.data.models.TrackIds
import com.example.playlistmaker.playlist.domain.api.PlayListInteractor
import com.example.playlistmaker.playlist.domain.models.PlayList
import com.example.playlistmaker.playlist.ui.models.PlayListsScreenState
import com.example.playlistmaker.playlist.ui.models.StateLocations
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.collections.ArrayList

class MediaPlayerViewModel(
    val clickedTrack: ClickedTrackGson,
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val favoriteTracksInteractor: FavoriteTracksInteractor,
    private val playListInteractor: PlayListInteractor,
    clickedTrackConverter: MapClickedTrackGsonToClickedTrack,
    private val json: Gson
) : ViewModel() {

    var playedTrack = clickedTrackConverter.map(clickedTrack)

    private var timerJob: Job? = null

    private fun preparePlayer() {
        mediaPlayerInteractor.prepare(playedTrack)
        checkingPreparePlayer()
    }


    private var mediaPlayerCurrentTimePlaying =
        SimpleDateFormat(
            "mm:ss", Locale.getDefault()
        ).format(mediaPlayerInteractor.getTimerStart())

    private val staeLocation: MutableLiveData<StateLocations> = MutableLiveData()

    fun getLocation(): LiveData<StateLocations> = staeLocation

    private val bottomSheetState: MutableLiveData<PlayListsScreenState> = MutableLiveData()
    fun getBootomSheetState(): LiveData<PlayListsScreenState> = bottomSheetState

    private var currentTrack =
        MutableLiveData<ClickedTrack>(clickedTrackConverter.map(clickedTrack))

    fun getCurrentTrack(): LiveData<ClickedTrack> = currentTrack

    private var playerScreenState = MutableLiveData<MediaPlayerScreenState>(
        MediaPlayerScreenState(
            mediaPlayerCurrentTimePlaying,
            mediaPlayerInteractor.getPlayerState()
        )
    )

    fun getAllPlayLists() {
        viewModelScope.launch(Dispatchers.IO) {
            playListInteractor.getAllPlayLists().collect {
                if (it.isNotEmpty()) bottomSheetState.postValue(
                    PlayListsScreenState.PlayListsContent(it)
                )
                else bottomSheetState.postValue(PlayListsScreenState.Empty)
            }
        }
    }

    fun getPlayerScreenState(): LiveData<MediaPlayerScreenState> = playerScreenState

    private var currentSingInFavorite: MutableLiveData<Boolean> =
        MutableLiveData(playedTrack.inFavorite)

    fun getCurrentSingFavorite(): LiveData<Boolean> = currentSingInFavorite

    fun changedSingInFavorite() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                favoriteTracksInteractor.changeSignFavorite(playedTrack.mapToTrack())
            }
            playedTrack.inFavorite = !playedTrack.inFavorite
            currentSingInFavorite.postValue(playedTrack.inFavorite)
        }

    }

    init {
        playerScreenState.value = MediaPlayerScreenState(
            mediaPlayerCurrentTimePlaying,
            mediaPlayerInteractor.getPlayerState()

        )
        viewModelScope.launch {
            currentSingInFavorite.postValue(playedTrack.inFavorite)
        }
        preparePlayer()
    }

    private fun startPlayer() {
        val currentPlayerStateState = getPlayerScreenState()
        mediaPlayerInteractor.play()
        updateTimerMedia()
        playerScreenState.value =
            currentPlayerStateState.value?.copy(playerState = mediaPlayerInteractor.getPlayerState())
    }

    fun checkLocationTrackInPL(playList: PlayList) {
        val ids = json.fromJson(playList.tracksIds, TrackIds::class.java)?: TrackIds(ArrayList())
        if (ids.data.contains(playedTrack.trackId.toString())) {
            staeLocation.postValue(StateLocations.isLocation(playList))
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                ids.data.add(playedTrack.trackId.toString())
                playList.tracksIds = json.toJson(ids.data)
                playListInteractor.saveTrack(playedTrack.mapToTrack(), playList)
                staeLocation.postValue(StateLocations.notLocation(playList))
            }
        }
    }

    fun pausePlayer() {
        val currentPlayerStateState = getPlayerScreenState()
        mediaPlayerInteractor.pause()
        timerJob?.cancel()
        playerScreenState.value =
            currentPlayerStateState.value?.copy(playerState = mediaPlayerInteractor.getPlayerState())
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayerInteractor.release()
    }

    private fun updateTimerMedia() {
        viewModelScope.launch {
            while (mediaPlayerInteractor.getPlayerState() == PlayerState.STATE_PLAYING) {
                delay(UPDATE_TIMER_TRACK)
                val currentTime = SimpleDateFormat(
                    "mm:ss", Locale.getDefault()
                ).format(mediaPlayerInteractor.getCurrentPosition())
                playerScreenState.value =
                    MediaPlayerScreenState(currentTime, mediaPlayerInteractor.getPlayerState())
            }
            if (mediaPlayerInteractor.getPlayerState() == PlayerState.STATE_PREPARED) {
                val currentTime = SimpleDateFormat(
                    "mm:ss", Locale.getDefault()
                ).format(mediaPlayerInteractor.getTimerStart())
                playerScreenState.value =
                    MediaPlayerScreenState(currentTime, mediaPlayerInteractor.getPlayerState())
            }
        }
    }

    private fun checkingPreparePlayer() {

        if (mediaPlayerInteractor.getPlayerState() != PlayerState.STATE_PREPARED) {
            viewModelScope.launch {
                while (mediaPlayerInteractor.getPlayerState() != PlayerState.STATE_PREPARED) {
                    delay(UPDATE_TIMER_TRACK)
                }
                playerScreenState.value = MediaPlayerScreenState(
                    mediaPlayerCurrentTimePlaying,
                    mediaPlayerInteractor.getPlayerState()
                )
            }
        }

    }

    fun playbackControl() {
        when (mediaPlayerInteractor.getPlayerState()) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
            }

            else -> {}
        }
    }

    companion object {
        private const val UPDATE_TIMER_TRACK = 300L
    }
}