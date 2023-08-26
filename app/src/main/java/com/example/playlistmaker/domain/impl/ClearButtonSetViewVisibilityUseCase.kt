package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SetViewVisibilityUseCase

class ClearButtonSetViewVisibilityUseCase: SetViewVisibilityUseCase {
    override fun execute(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }
}