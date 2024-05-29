package com.example.newagilityapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Project(
    val name: String,
    val endDate:String,
    val goalHours: Float,
    @PrimaryKey(autoGenerate = true)
    val projectId : Int? = null,
    val description: String
)
