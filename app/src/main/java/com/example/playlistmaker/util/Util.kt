package com.example.playlistmaker.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


fun <T> debounce(delayMillis: Long,
                 coroutineScope: CoroutineScope,
                 useLastParam: Boolean,
                 action: (T) -> Unit): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        if (useLastParam) {
            debounceJob?.cancel()
        }
        if (debounceJob?.isCompleted != false || useLastParam) {
            debounceJob = coroutineScope.launch {
                delay(delayMillis)
                action(param)
            }
        }
    }
}

fun getEndMessage(count: Int): String {
    val endMessage = when (count % 100) {
        in 11..19 -> "треков"
        else -> {
            when (count % 10) {
                1 -> "трек"
                in 2..4 -> "трека"
                else -> "треков"
            }
        }
    }
    return endMessage
}