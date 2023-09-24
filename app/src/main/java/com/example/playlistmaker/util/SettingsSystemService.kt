package com.example.playlistmaker.util

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.TypedValue
import android.view.View
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

private val correction = 0.5f
fun Context.setVibe() {
    val vibe: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    vibe.vibrate(VibrationEffect.createOneShot(5, VibrationEffect.DEFAULT_AMPLITUDE))
}

fun View.setOnClickListenerWithViber(block: () -> Unit) {
    setOnClickListener {
        context.setVibe()
        block()
    }
}

internal fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp + correction,
        context.resources.displayMetrics
    ).toInt()
}

internal fun pxToDp(px: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_PX,
        px,
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