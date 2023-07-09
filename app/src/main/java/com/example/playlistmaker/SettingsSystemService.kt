package com.example.playlistmaker
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View

fun Context.setVibe(){
    val vibe: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    vibe.vibrate(VibrationEffect.createOneShot(5, VibrationEffect.DEFAULT_AMPLITUDE))
}

fun View.setOnClickListenerWithViber(block: () -> Unit) {
    setOnClickListener {
        context.setVibe()
        block()
    }
}