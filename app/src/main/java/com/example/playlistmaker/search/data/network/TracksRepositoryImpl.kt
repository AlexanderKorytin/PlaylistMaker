package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.TrackRequest
import com.example.playlistmaker.search.data.dto.TracksResponse
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.consumer.Consumer
import com.example.playlistmaker.search.domain.consumer.ConsumerData
import com.example.playlistmaker.search.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun getMusic(term: String, consumer: Consumer<List<Track>>) {
        val t = Thread {
            val response = networkClient.searchTracks(TrackRequest(term))
            when (response?.resultCode) {
                -1 -> {
                    consumer.consume(ConsumerData.Error("No Internet"))
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
                        consumer.consume(ConsumerData.Data(resultSearch))
                    } else {
                        val resultSearch = emptyList<Track>()
                        consumer.consume(ConsumerData.Data(resultSearch))
                    }
                }

                else -> {
                    consumer.consume(ConsumerData.Error("Server Error"))
                }

            }
        }.start()
    }
}