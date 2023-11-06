package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.SearchTracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.SearchResultData
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchTracksInteractorImpl(private val trackRepository: TracksRepository) :
    SearchTracksInteractor {

    override fun getMusic(term: String): Flow<Pair<List<Track>?, String?>> {
        return trackRepository.getMusic(term).map { result ->
            when (result) {
                is SearchResultData.Data -> {
                    Pair(result.value, null)
                }

                is SearchResultData.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

}