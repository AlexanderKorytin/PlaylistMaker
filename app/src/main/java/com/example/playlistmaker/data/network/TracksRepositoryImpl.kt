package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.TrackRequest
import com.example.playlistmaker.data.dto.TracksResponse
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun getMusic(term: String): List<Track> {
        val response = networkClient.searchTracks(TrackRequest(term))
        if (response.resultCode == 200) {
            return (response as TracksResponse).results.map {
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
        } else {
            return emptyList()
        }
    }
}