package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.TrackRequest
import com.example.playlistmaker.search.data.dto.TracksResponse
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.consumer.Consumer
import com.example.playlistmaker.search.domain.consumer.ConsumerData
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun getMusic(term: String):Flow<ConsumerData<List<Track>>>  = flow{
            val response = networkClient.searchTracks(TrackRequest(term))
            when (response?.resultCode) {
                -1 -> {
                    emit(ConsumerData.Error("No Internet"))
                }

                200 -> {
                    if ((response as TracksResponse).results.isNotEmpty()) {
                        val resultSearch = response.results.map {
                            Track(
                                it.trackId,
                                it.getParam(it.trackName),
                                it.getParam(it.artistName),
                                it.getTrackTime(),
                                it.getParam(it.artworkUrl100),
                                it.getParam(it.country),
                                it.getParam(it.collectionName),
                                it.getYear(),
                                it.getParam(it.primaryGenreName),
                                it.getParam(it.previewUrl),
                                it.getCoverArtwork()
                            )
                        }
                        emit(ConsumerData.Data(resultSearch))
                    } else {
                        val resultSearch = emptyList<Track>()
                        emit(ConsumerData.Data(resultSearch))
                    }
                }

                else -> {
                    emit(ConsumerData.Error("Server Error"))
                }

            }
    }
}