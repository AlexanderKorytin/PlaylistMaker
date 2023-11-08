package com.example.playlistmaker.app

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.TypedValue
import android.view.View
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.reflect.Type

private val correction = 0.5f

internal fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp + correction,
        context.resources.displayMetrics
    ).toInt()
}

internal fun getTrackListFromJson(json: String?): ArrayList<Track> {
    if (json != null) {
        val type: Type = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, type)
    } else {
        return arrayListOf()
    }
}