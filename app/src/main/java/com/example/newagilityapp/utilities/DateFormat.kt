package com.example.newagilityapp.utilities

fun formatDuration(duration: Long): String {
    val hours = duration / 3600
    val minutes = (duration % 3600) / 60
    val seconds = duration % 60

    return if (hours > 0) {
        // Show hours and minutes if the duration is one hour or more
        String.format(java.util.Locale.getDefault(), "%02d:%02d", hours, minutes)
    } else {
        // Show minutes and seconds if the duration is less than one hour
        String.format(java.util.Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }
}
fun formatGoalHours(hours: Float): String {
    val hoursInt = hours.toInt()
    val minutes = ((hours - hoursInt) * 60).toInt()
    return String.format(java.util.Locale.getDefault(), "%02d:%02d", hoursInt, minutes)
}
