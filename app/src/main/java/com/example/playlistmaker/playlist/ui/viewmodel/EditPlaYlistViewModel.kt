package com.example.playlistmaker.playlist.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.currentplaylist.domain.api.CurrentPlayListInteractor
import com.example.playlistmaker.playlist.domain.api.PlayListInteractor
import com.example.playlistmaker.playlist.domain.models.PlayList
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val playListId: Int,
    private val playListInteractor: PlayListInteractor,
    private val currentPlayListInteractor: CurrentPlayListInteractor,
    override val json: Gson
) : PlayListsViewModel(playListInteractor, json) {

    private lateinit var playlist: PlayList
    fun getPlayListVM(): PlayList{
        return playlist
    }

    private var screenState: MutableLiveData<PlayList> = MutableLiveData()

    fun getCurrentScreenState(): LiveData<PlayList> = screenState
    override fun savePlayList(playList: PlayList) {
        viewModelScope.launch(Dispatchers.IO) {
            playListInteractor.savePlayList(playList)
        }
    }

    fun getPlayList() {
        viewModelScope.launch(Dispatchers.IO) {
            playlist = currentPlayListInteractor.getPlayListById(playListId)
            screenState.postValue(playlist)
        }
    }
}