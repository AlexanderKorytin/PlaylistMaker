package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SetViewVisibilityUseCase

class SetViewVisibilityClearButton: SetViewVisibilityUseCase {
    override fun execute(s: Any?): Boolean {
        return (s as CharSequence).isNotEmpty()
    }
}