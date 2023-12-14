package com.example.playlistmaker.currentplaylist.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.currentplaylist.domain.api.CurrentPlayListInteractor
import com.example.playlistmaker.currentplaylist.ui.models.CurrentPlayListScreenState
import com.example.playlistmaker.playlist.domain.models.PlayList
import com.example.playlistmaker.search.ui.mappers.MapToTrackUI
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrentPlayListViewModel(
    val playList: String?,
    private val currentPlayListInteractor: CurrentPlayListInteractor,
    val mapper: MapToTrackUI,
    val json: Gson
) : ViewModel() {

    private val currentPlayList = json.fromJson(playList, PlayList::class.java)

    private val currentPlayListScreenState: MutableLiveData<CurrentPlayListScreenState> =
        MutableLiveData()

    fun getCurrentScreenState(): LiveData<CurrentPlayListScreenState> = currentPlayListScreenState

    fun getTracks() {
        viewModelScope.launch(Dispatchers.IO) {
            currentPlayListInteractor.getTracksFromPlailist(playListId = currentPlayList.playListId)
                .collect { result ->
                    when {
                        result.isEmpty() -> {
                            currentPlayListScreenState.postValue(
                                CurrentPlayListScreenState.Empty(
                                    currentPlayList.playListName
                                )
                            )
                        }

                        else -> {
                            currentPlayListScreenState.postValue(
                                CurrentPlayListScreenState.Content(
                                    playListName = currentPlayList.playListName,
                                    result
                                )
                            )
                        }
                    }
                }
        }
    }

}