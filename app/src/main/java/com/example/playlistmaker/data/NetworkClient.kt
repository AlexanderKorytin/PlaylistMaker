package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.Responce

interface NetworkClient {
    fun doRequest(dto: Any): Responce
}