package com.example.newagilityapp.model

data class Task(
    val title:String,
    val description: String,
    val endate: Long,
    val fromProject:String,
    val isDone: Boolean
)
