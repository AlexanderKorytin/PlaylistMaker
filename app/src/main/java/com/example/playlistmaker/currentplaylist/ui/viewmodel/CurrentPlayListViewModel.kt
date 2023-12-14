package com.example.playlistmaker.currentplaylist.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.currentplaylist.domain.api.CurrentPlayListInteractor
import com.example.playlistmaker.currentplaylist.ui.models.CurrentPlayListScreenState
import com.example.playlistmaker.favoritetracks.domain.api.FavoriteTracksInteractor
import com.example.playlistmaker.playlist.domain.models.PlayList
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrentPlayListViewModel(
    val playList: String?,
    private val currentPlayListInteractor: CurrentPlayListInteractor,
    private val favoriteTracksInteractor: FavoriteTracksInteractor
) : ViewModel() {

    private val currentPlayListScreenState: MutableLiveData<CurrentPlayListScreenState> =
        MutableLiveData()

    fun getCurrentScreenState(): LiveData<CurrentPlayListScreenState> = currentPlayListScreenState

    fun getTracks(playList: PlayList) {
        viewModelScope.launch(Dispatchers.IO) {
            currentPlayListInteractor.getTracksFromPlailist(playListId = playList.playListId)
                .collect { result ->
                    when {
                        result.isEmpty() -> {
                            currentPlayListScreenState.postValue(
                                CurrentPlayListScreenState.Empty(
                                    playList.playListName
                                )
                            )
                        }

                        else -> {
                            val favoriteList = mutableListOf<Track>()
                            favoriteTracksInteractor.getFavoriteTracks().collect {
                                favoriteList.addAll(it)
                            }
                            val list = mutableListOf<Track>()
                            list.addAll(result)
                            list.map { it.inFavorite = favoriteList.contains(it) }
                            currentPlayListScreenState.postValue(
                                CurrentPlayListScreenState.Content(
                                    playListName = playList.playListName,
                                    list
                                )
                            )
                        }
                    }
                }
        }
    }

}