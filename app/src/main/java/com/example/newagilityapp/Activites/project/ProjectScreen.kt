package com.example.newagilityapp.Activites.project

import android.health.connect.datatypes.units.Percentage
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.newagilityapp.Activites.dashboard.components.CountCard
import com.example.newagilityapp.Activites.dashboard.components.projectSessionsList
import com.example.newagilityapp.Activites.dashboard.components.taskList
import com.example.newagilityapp.sesions
import com.example.newagilityapp.tasks
import javax.security.auth.Subject

@Composable
fun ProjectScreen(){
    Scaffold(
        topBar = { ProjectScreenTopBar(
            title = "Aldi",
            onBackClick = {},
            onDeleteClick = {},
            onEditClick = {}
            )
        }
    ) {paddingValue ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ){
            item {
                ProjectOverviewSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    hoursDone = "10",
                    goalHours = "20",
                    progress = 0.20f
                )
            }
            taskList(
                sectionTitle = "Trabajos por hacer",
                emptyListText = "No tienes trabajos por hacer. \n Pulsa el botón + para crear una nueva",
                tasks = tasks,
                onCheckBoxClick = {},
                onTaskCardClick = {}
            )
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            taskList(
                sectionTitle = "Trabajos Completados",
                emptyListText = "No tienes trabajos completados. \n Pulsa el botón + para crear una nueva",
                tasks = tasks,
                onCheckBoxClick = {},
                onTaskCardClick = {}
            )
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            projectSessionsList(
                sectionTitle = "Sesiones de los proyectos",
                emptyListText = "No tienes ninguna sesión de Proyectos.\n !Añade una ahora¡",
                sessions = sesions,
                onDeleteIconClick = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProjectScreenTopBar(
    title: String,
    onBackClick:() -> Unit,
    onDeleteClick:() -> Unit,
    onEditClick:() -> Unit
){
    LargeTopAppBar(
        navigationIcon = {
            IconButton(onClick = { onBackClick }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Ir atras"
                )
            }
        },
        title = { Text(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineLarge
        )},
        actions = {
            IconButton(onClick = { onDeleteClick }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Borrar Proyecto"
                )
            }
            IconButton(onClick = { onEditClick }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar Proyecto"
                )
            }
        }
    )
}

@Composable
private fun ProjectOverviewSection(
    modifier: Modifier,
    hoursDone: String,
    goalHours:String,
    progress: Float
){
    val percentage = remember(progress){
        (progress*100).toInt().coerceIn(0,100)
    }

    Row (
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ){
        CountCard(
            modifier = Modifier.weight(1f),
            title = "Objetivo de Horas",
            count = goalHours
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            title = "Horas Hechas",
            count = hoursDone
        )
        Spacer(modifier = Modifier.width(10.dp))
        Box (
            modifier = Modifier.size(75.dp),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                progress = 1f,
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                progress = progress,
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round
            )
            Text(text = "$percentage%")
        }
    }
}