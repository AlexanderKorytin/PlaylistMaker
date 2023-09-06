package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.SetViewVisibilityUseCase

class ClearButtonSetViewVisibilityUseCase: SetViewVisibilityUseCase {
    override fun execute(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }
}