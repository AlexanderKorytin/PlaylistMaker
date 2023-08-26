package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackRequest
import com.example.playlistmaker.data.dto.TracksResponse
import retrofit2.Callback

interface NetworkClient {

    fun searchTracks(request: TrackRequest): Response

}
