package com.example.newagilityapp.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.newagilityapp.utilities.Priority

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Project::class,
            parentColumns = ["projectId"],
            childColumns = ["taskProjectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["taskProjectId"])
    ]
)
data class Task(
    val title: String,
    val description: String,
    val endate: Long,
    val taskProjectId: Int,
    val fromProject: String,
    val isDone: Boolean,
    @PrimaryKey(autoGenerate = true)
    val taskId: Int? = null,
    val priority: Priority
)
