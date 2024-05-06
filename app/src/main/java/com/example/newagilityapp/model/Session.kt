package com.example.newagilityapp.model

data class Session(
    val sessionId: Int,
    val fromProject: String,
    val date: Long,
    val duration: Long,
    val projectSessionId: Int
    )
