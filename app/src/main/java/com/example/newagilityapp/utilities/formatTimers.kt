package com.example.newagilityapp.utilities

import java.util.Locale

fun formatDuration(duration: Long): String {
    val hours = duration / 3600
    val minutes = (duration % 3600) / 60
    val seconds = duration % 60

    return if (hours > 0) {
        String.format(Locale.getDefault(), "%02d:%02d", hours, minutes)
    } else {
        String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }
}
fun formatGoalHours(hours: Float): String {
    val hoursInt = hours.toInt()
    val minutes = ((hours - hoursInt) * 60).toInt()
    return String.format(Locale.getDefault(), "%02d:%02d", hoursInt, minutes)
}

fun formatElapsedTime(elapsedTime: Long): String {
    val hours = elapsedTime / 3600
    val minutes = (elapsedTime % 3600) / 60
    val seconds = elapsedTime % 60
    return if (hours > 0) {
        String.format(Locale.getDefault(), "%02d:%02d", hours, minutes)
    } else {
        String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }
}