package com.example.newagilityapp.model

data class Task(
    val title:String,
    val description: String,
    val endate: Long,
    val taskProjectId: Int,
    val fromProject:String,
    val isDone: Boolean,
    val TaskId: Int,
    val priority: Int
)
