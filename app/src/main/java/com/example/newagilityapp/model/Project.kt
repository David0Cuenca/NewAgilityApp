package com.example.newagilityapp.model

import androidx.compose.material3.CardColors
import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newagilityapp.ui.theme.gradient1
import com.example.newagilityapp.ui.theme.gradient2
import com.example.newagilityapp.ui.theme.gradient3
import com.example.newagilityapp.ui.theme.gradient4
import com.example.newagilityapp.ui.theme.gradient5

@Entity
data class Project(
    val name: String,
    val EndDate:String,
    val colors: List<Color>,
    val goalHours: Float,
    @PrimaryKey(autoGenerate = true)
    val projectId : Int? = null
){
    companion object {
        val CardColors = listOf(gradient1, gradient2, gradient3, gradient4, gradient5)
    }
}

