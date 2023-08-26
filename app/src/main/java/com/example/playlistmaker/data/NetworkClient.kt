package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackRequest

interface NetworkClient {
    fun searchTracks(request: TrackRequest): Response
}
