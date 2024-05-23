package com.example.newagilityapp.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.newagilityapp.data.local.ListConverter
import com.example.newagilityapp.ui.theme.gradient1
import com.example.newagilityapp.ui.theme.gradient2
import com.example.newagilityapp.ui.theme.gradient3
import com.example.newagilityapp.ui.theme.gradient4
import com.example.newagilityapp.ui.theme.gradient5

@Entity
data class Project(
    val name: String,
    val endDate:String,
    val goalHours: Float,
    @PrimaryKey(autoGenerate = true)
    val projectId : Int? = null
)
