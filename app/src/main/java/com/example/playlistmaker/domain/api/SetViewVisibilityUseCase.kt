package com.example.playlistmaker.domain.api

interface SetViewVisibilityUseCase {
    fun execute(s: CharSequence?): Boolean
}