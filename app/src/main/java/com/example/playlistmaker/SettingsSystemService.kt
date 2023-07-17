package com.example.playlistmaker
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.TypedValue
import android.view.View
val correction = 0.5f
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

fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp+ correction,
        context.resources.displayMetrics).toInt()
}