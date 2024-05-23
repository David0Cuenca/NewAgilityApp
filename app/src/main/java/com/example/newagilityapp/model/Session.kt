package com.example.newagilityapp.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Project::class,
            parentColumns = ["projectId"],
            childColumns = ["projectSessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Session(
    @PrimaryKey(autoGenerate = true)
    val sessionId: Int? = null,
    val fromProject: String,
    val date: String,
    val duration: Long,
    val projectSessionId: Int
    )
