package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.SearchTracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.consumer.ConsumerData
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchTracksInteractorImpl(private val trackRepository: TracksRepository) :
    SearchTracksInteractor {

    override fun getMusic(term: String): Flow<Pair<List<Track>?, String?>> {
        return trackRepository.getMusic(term).map { result ->
            when (result) {
                is ConsumerData.Data -> {
                    Pair(result.value, null)
                }

                is ConsumerData.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

}