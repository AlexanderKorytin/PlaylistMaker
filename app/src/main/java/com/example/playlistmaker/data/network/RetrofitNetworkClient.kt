package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackRequest
import com.example.playlistmaker.data.dto.TracksResponse
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {
    private val baseUrlAppleMusic = " https://itunes.apple.com"
    private val retrofit = Retrofit
        .Builder()
        .baseUrl(baseUrlAppleMusic)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesApi::class.java)
    override fun searchTracks(request: TrackRequest): Response {
        val response = iTunesService.searchTracks(request.term).execute()
        val body = response.body() ?: Response()
        return body.apply { resultCode = response.code() }
    }
}