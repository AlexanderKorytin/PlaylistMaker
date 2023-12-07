package com.example.playlistmaker.playlist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.playlist.domain.api.PlayListInteractor
import com.example.playlistmaker.playlist.domain.models.PlayList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayListsViewModel(private val interactor: PlayListInteractor) : ViewModel() {
    fun savePlayList(playList: PlayList) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.savePlayListToDb(playList)
        }
    }
}