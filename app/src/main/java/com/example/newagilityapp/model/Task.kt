package com.example.newagilityapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    val title:String,
    val description: String,
    val endate: Long,
    val taskProjectId: Int,
    val fromProject:String,
    val isDone: Boolean,
    @PrimaryKey(autoGenerate = true)
    val taskId: Int? = null,
    val priority: Int
)
