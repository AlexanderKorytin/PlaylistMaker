package com.example.playlistmaker.player.ui.viewmodel

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.currentplaylist.domain.api.CurrentPlayListInteractor
import com.example.playlistmaker.favoritetracks.domain.api.FavoriteTracksInteractor
import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.models.ClickedTrack
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.ui.mappers.MapClickedTrackGsonToClickedTrack
import com.example.playlistmaker.player.ui.models.ClickedTrackGson
import com.example.playlistmaker.player.ui.models.MediaPlayerScreenState
import com.example.playlistmaker.playlist.domain.api.PlayListInteractor
import com.example.playlistmaker.playlist.domain.models.PlayList
import com.example.playlistmaker.playlist.ui.models.PlayListsScreenState
import com.example.playlistmaker.playlist.ui.models.ToastStase
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class MediaPlayerViewModel(
    val clickedTrack: ClickedTrackGson,
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val favoriteTracksInteractor: FavoriteTracksInteractor,
    private val playListInteractor: PlayListInteractor,
    clickedTrackConverter: MapClickedTrackGsonToClickedTrack,
    private val currentPlayListInteractor: CurrentPlayListInteractor,
    val analytics: FirebaseAnalytics
) : ViewModel() {

    var playedTrack = clickedTrackConverter.map(clickedTrack)

    private var timerJob: Job? = null

    private suspend fun preparePlayer() {
        viewModelScope.launch(Dispatchers.IO) {
            val listId = currentPlayListInteractor.getPlayListIdsCurrentTrack(
                playedTrack.trackId
            )
            val newPlayedTrack = playedTrack.copy(playListIds = listId)
            playedTrack = newPlayedTrack
            async {
                mediaPlayerInteractor.prepare(playedTrack)
            }.await()
        }
        checkingPreparePlayer()
    }


    private var _mediaPlayerCurrentTimePlaying =
        SimpleDateFormat(
            "mm:ss", Locale.getDefault()
        ).format(mediaPlayerInteractor.getTimerStart())

    private val _toastState: MutableLiveData<ToastStase> = MutableLiveData()

    fun getToastState(): LiveData<ToastStase> = _toastState

    private val _bottomSheetState: MutableLiveData<PlayListsScreenState> = MutableLiveData()
    fun getBootomSheetState(): LiveData<PlayListsScreenState> = _bottomSheetState

    private var _currentTrack =
        MutableLiveData<ClickedTrack>(clickedTrackConverter.map(clickedTrack))

    fun getCurrentTrack(): LiveData<ClickedTrack> = _currentTrack

    private var _playerScreenState = MutableLiveData<MediaPlayerScreenState>(
        MediaPlayerScreenState(
            _mediaPlayerCurrentTimePlaying,
            mediaPlayerInteractor.getPlayerState()
        )
    )

    fun getAllPlayLists() {
        viewModelScope.launch(Dispatchers.IO) {
            playListInteractor.getAllPlayLists().collect {
                if (it.isNotEmpty()) _bottomSheetState.postValue(
                    PlayListsScreenState.PlayListsContent(it)
                )
                else _bottomSheetState.postValue(PlayListsScreenState.Empty)
            }
        }
    }

    fun getPlayerScreenState(): LiveData<MediaPlayerScreenState> = _playerScreenState

    private var currentSingInFavorite: MutableLiveData<Boolean> =
        MutableLiveData(playedTrack.inFavorite)

    fun getCurrentSingFavorite(): LiveData<Boolean> = currentSingInFavorite

    fun changedSingInFavorite() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                favoriteTracksInteractor.changeSignFavorite(playedTrack.mapClickedTrackToTrack())
            }
            val newTrack = playedTrack.copy(inFavorite = !playedTrack.inFavorite)
            playedTrack = newTrack
            currentSingInFavorite.postValue(playedTrack.inFavorite)
        }

    }

    init {
        _playerScreenState.value = MediaPlayerScreenState(
            _mediaPlayerCurrentTimePlaying,
            mediaPlayerInteractor.getPlayerState()

        )
        viewModelScope.launch {
            currentSingInFavorite.postValue(playedTrack.inFavorite)
            preparePlayer()
        }
    }

    private fun startPlayer() {
        val currentPlayerStateState = getPlayerScreenState()
        mediaPlayerInteractor.play()
        updateTimerMedia()
        _playerScreenState.value =
            currentPlayerStateState.value?.copy(playerState = mediaPlayerInteractor.getPlayerState())
    }

    fun checkLocationTrackInPL(playList: PlayList, track: ClickedTrack) {
        if (playList.tracksIds.contains(playedTrack.trackId)) {
            viewModelScope.launch(Dispatchers.IO) {
                currentPlayListInteractor.saveTrackPlayListIdsChange(
                    track.mapClickedTrackToTrack(),
                    playList.playListId
                )
            }
            _toastState.postValue(ToastStase.isLocation(playList))
        } else {
            playList.tracksIds.add(playedTrack.trackId)
            track.playListIds.add(playList.playListId)
            viewModelScope.launch(Dispatchers.IO) {
                playListInteractor.saveTrack(playedTrack.mapClickedTrackToTrack(), playList)
                _toastState.postValue(ToastStase.notLocation(playList))
            }
        }
    }

    fun pausePlayer() {
        val currentPlayerStateState = getPlayerScreenState()
        mediaPlayerInteractor.pause()
        timerJob?.cancel()
        _playerScreenState.value =
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
                _playerScreenState.value =
                    MediaPlayerScreenState(currentTime, mediaPlayerInteractor.getPlayerState())
            }
            if (mediaPlayerInteractor.getPlayerState() == PlayerState.STATE_PREPARED) {
                val currentTime = SimpleDateFormat(
                    "mm:ss", Locale.getDefault()
                ).format(mediaPlayerInteractor.getTimerStart())
                _playerScreenState.value =
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
                _playerScreenState.value = MediaPlayerScreenState(
                    _mediaPlayerCurrentTimePlaying,
                    mediaPlayerInteractor.getPlayerState()
                )
            }
        }

    }

    fun toastWasShown() {
        _toastState.value = ToastStase.None
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