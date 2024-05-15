package com.example.newagilityapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Session(
    @PrimaryKey(autoGenerate = true)
    val sessionId: Int? = null,
    val fromProject: String,
    val date: Long,
    val duration: Long,
    val projectSessionId: Int
    )
