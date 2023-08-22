package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Responce
import com.example.playlistmaker.data.dto.TrackRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient: NetworkClient {
    private val baseUrlAppleMusic = " https://itunes.apple.com"
    private val retrofit = Retrofit
        .Builder()
        .baseUrl(baseUrlAppleMusic)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesApi::class.java)
    override fun doRequest(dto: Any): Responce {
        if (dto is TrackRequest){
            val response = iTunesService.searchTracks(dto.term).execute()
            val body = response.body() ?: Responce()
            return body.apply { resultCode = response.code() }
        } else {
            return Responce().apply { resultCode = 400 }
        }
    }
}