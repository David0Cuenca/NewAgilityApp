package com.example.newagilityapp.activites.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.newagilityapp.R

@Composable
fun ProjectCard(
    modifier: Modifier = Modifier,
    projectname: String,
    onClick:() -> Unit
){
    //Todo hacer la cajeta de los proyectos distinta
    Box(
        modifier = Modifier.size(150.dp)
            .size(150.dp)
            .clickable {onClick()}
            .background(
                brush = Brush.verticalGradient(listOf(MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.scrim)),
                shape = MaterialTheme.shapes.medium
            )
    ){
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
        Image(
            painter = painterResource(R.drawable.project_icon),
            contentDescription = projectname,
            modifier = Modifier.size(80.dp)
            )
            Text(
                text = projectname,
                color = MaterialTheme.colorScheme.inversePrimary,
                style = MaterialTheme.typography.headlineMedium,
            )
        }
    }
}