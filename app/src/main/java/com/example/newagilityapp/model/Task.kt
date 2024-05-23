package com.example.newagilityapp.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Project::class,
            parentColumns = ["projectId"],
            childColumns = ["taskProjectId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Task(
    val title:String,
    val description: String,
    val endate: Long,
    val taskProjectId: Int,
    val isDone: Boolean,
    @PrimaryKey(autoGenerate = true)
    val taskId: Int? = null,
    val priority: Int
)
