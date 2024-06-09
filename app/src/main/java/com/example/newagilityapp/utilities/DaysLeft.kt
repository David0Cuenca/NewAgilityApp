package com.example.newagilityapp.utilities

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun daysLeft(endDate: String?): Pair<Int, String> {
    if (endDate.isNullOrBlank()) return Pair(0, "No hay fecha final")

    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    val today = LocalDate.now()
    val end = LocalDate.parse(endDate, formatter)

    val totalDays = ChronoUnit.DAYS.between(today, end).toInt()
    val formattedTime = when {
        totalDays == 0 -> "Entregar hoy"
        totalDays < 0 -> "Retraso en la entrega"
        totalDays >= 7 -> {
            val weeks = totalDays / 7
            val days = totalDays % 7
            "$weeks semanas y $days días"
        }
        else -> "$totalDays días"
    }

    return Pair(totalDays, formattedTime)
}