package com.example.playlistmaker.search.domain.api

interface SetViewVisibilityUseCase {
    fun execute(s: CharSequence?): Boolean
}