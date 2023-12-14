package com.example.playlistmaker.playlist.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.playlist.domain.api.PlayListInteractor
import com.example.playlistmaker.playlist.domain.models.PlayList
import com.example.playlistmaker.playlist.ui.models.PlayListsScreenState
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayListsViewModel(
    private val interactor: PlayListInteractor,
    val json: Gson
) : ViewModel() {

    private val screenState: MutableLiveData<PlayListsScreenState> = MutableLiveData()

    fun getScreenState(): LiveData<PlayListsScreenState> = screenState

    fun savePlayList(playList: PlayList) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.savePlayListToDb(playList)
        }
    }

    fun getAllPlayLists() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.getAllPlayLists().collect { result ->
                if (result.isNotEmpty()) {
                    screenState.postValue(PlayListsScreenState.PlayListsContent(result))
                } else {
                    screenState.postValue(PlayListsScreenState.Empty)
                }
            }
        }
    }
}