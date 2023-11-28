package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.SearchTracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.Result
import com.example.playlistmaker.search.domain.models.SearchResultData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchTracksInteractorImpl(private val trackRepository: TracksRepository) :
    SearchTracksInteractor {

    override fun getMusic(term: String): Flow<Result> {
        return trackRepository.getMusic(term).map { result ->
            when (result) {
                is SearchResultData.Data -> {
                    Result(result.value, null)
                }

                is SearchResultData.Error -> {
                    Result(null, result.message)
                }
            }
        }
    }

}