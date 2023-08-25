package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SetViewVisibility

class SetViewVisibilityClearButton: SetViewVisibility {
    override fun execute(s: Any?): Boolean {
        return (s as CharSequence).isNotEmpty()
    }
}