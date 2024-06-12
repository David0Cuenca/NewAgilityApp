package com.example.newagilityapp.utilities

import androidx.compose.ui.graphics.Color

enum class Priority(val title: String, val color: Color, val value: Int) {
    LOW(title = "Baja", color = Color.Green, value = 0),
    MEDIUM(title = "Medio", color = Color.Yellow, value = 1),
    HIGH(title = "Alta", color = Color.Red, value = 2);

    companion object {
        fun fromInt(value: Int) = entries.firstOrNull { it.value == value } ?: MEDIUM
    }
}