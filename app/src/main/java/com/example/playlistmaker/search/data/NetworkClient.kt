package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TrackRequest

interface NetworkClient {

   suspend fun searchTracks(request: TrackRequest): Response?

}
