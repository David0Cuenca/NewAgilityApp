package com.example.newagilityapp.Activites.dashboard

import android.icu.text.CaseMap.Title
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.newagilityapp.Activites.dashboard.components.CountCard
import com.example.newagilityapp.Activites.dashboard.components.DeleteDialog
import com.example.newagilityapp.Activites.dashboard.components.NewProjectDialog
import com.example.newagilityapp.Activites.dashboard.components.ProjectCard

import com.example.newagilityapp.Activites.dashboard.components.projectSessionsList
import com.example.newagilityapp.Activites.dashboard.components.taskList
import com.example.newagilityapp.R
import com.example.newagilityapp.model.Project
import com.example.newagilityapp.model.Session
import com.example.newagilityapp.model.Task

@Composable
fun DashboardScreen(){

    val projects = listOf(
        Project(name = "Aldi", EndDate = "12/12/2020", colors = Project.CardColors[1], projectId = 0),
        Project(name = "Consejos", EndDate = "12/12/2020", colors = Project.CardColors[1], projectId = 0),
        Project(name = "Clases", EndDate = "12/12/2020", colors = Project.CardColors[1], projectId = 0),
    )

    val tasks = listOf(
        Task(title = "Hacer cosas", description = "Seguir asi", endate = 0L, taskProjectId = 0, fromProject = "",false, TaskId = 0, priority = 0),
        Task(title = "Hacer cosas", description = "Seguir asi", endate = 0L, taskProjectId = 0, fromProject = "",false, TaskId = 0, priority = 1),
        Task(title = "Hacer cosas", description = "Seguir asi", endate = 0L, taskProjectId = 0, fromProject = "",true, TaskId = 0, priority = 2),
        Task(title = "Hacer cosas", description = "Seguir asi", endate = 0L, taskProjectId = 0, fromProject = "",false, TaskId = 0, priority = 3),
        Task(title = "Hacer cosas", description = "Seguir asi", endate = 0L, taskProjectId = 0, fromProject = "",true, TaskId = 0, priority = 1),
    )

    val sesions = listOf(
        Session(fromProject = "Aldi", date = 0L, duration = 2, projectSessionId = 0, sessionId = 0),
        Session(fromProject = "Cien", date = 0L, duration = 2, projectSessionId = 0, sessionId = 0),
        Session(fromProject = "Aldi", date = 0L, duration = 2, projectSessionId = 0, sessionId = 0),
        Session(fromProject = "Agility", date = 0L, duration = 2, projectSessionId = 0, sessionId = 0),
        Session(fromProject = "Aldi", date = 0L, duration = 2, projectSessionId = 0, sessionId = 0),
    )

    var isOpenNewProject by rememberSaveable { mutableStateOf(false) }

    var isOpenDelete by rememberSaveable { mutableStateOf(false) }

    var projectName by remember { mutableStateOf("") }
    var goalHours by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(Project.CardColors.random()) }

    NewProjectDialog(
        isOpen = isOpenNewProject,
        title = "",
        selectedColors = selectedColor,
        projectname = projectName,
        goalHours = goalHours,
        onColorChange = {selectedColor = it},
        onProjectNameChange = {projectName = it},
        onGoalHoursChange = {goalHours = it},
        onDismissRequest = { isOpenNewProject = false },
        onConfirmButtonsClick = {
            isOpenNewProject = false
        },

    )

    DeleteDialog(
        isOpen = isOpenDelete,
        title = "Borrar sesión",
        text = "Vas a borrar una sessión de un proyecto \n " +
                "¿Estas seguro de hacerlo? Recuerda que esto no se puede revertir una vez hecho",
        onDismissRequest = { isOpenDelete = false },
        onConfirmButtonsClick = {isOpenDelete = false}
    )

    Scaffold(
        topBar = { DashboardScreenTopBar() }
    ) {paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ){
            item {
                CountCardSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    nproyectos = 2,
                    fproyectos = 3,
                    hours = 12)
            }
            item {
                ProjectsCardSection(
                    modifier = Modifier.fillMaxWidth(),
                    projectList = projects,
                    onAddIconClicked = {
                        isOpenNewProject = true
                    }
                )
            }
            item {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp, vertical = 20.dp)
                ) {
                    Text(text = "Creación de proyectos")
                }
            }
            taskList(
                sectionTitle = "Trabajos por hacer",
                emptyListText = "No tienes trabajos por hacer. \n Pulsa el botón + para crear una nueva",
                tasks = tasks,
                onCheckBoxClick = {},
                onTaskCardClick = {}
            )
            item { 
                Spacer(modifier = Modifier.size(20.dp))
            }
            projectSessionsList(
                sectionTitle = "Sesiones de los proyectos",
                emptyListText = "No tienes ninguna sesión de Proyectos.\n !Añade una ahora¡",
                sessions = sesions,
                onDeleteIconClick = {isOpenDelete = true}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardScreenTopBar(){
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "New Agility",
                style = MaterialTheme.typography.headlineLarge
            )
        }
    )
}

@Composable
private fun CountCardSection(
    modifier: Modifier,
    nproyectos: Int,
    fproyectos: Int,
    hours: Int,
){
    Row {
        CountCard(
            modifier = Modifier.weight(1f),
            title = "Proyectos totales",
            count = "$nproyectos"
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            title = "Terminados",
            count = "$fproyectos"
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            title = "Horas de trabajo",
            count = "$hours"
        )
    }
}
@Composable
private fun ProjectsCardSection(
    modifier: Modifier,
    projectList: List<Project>,
    emptyListText:String = "No tienes ningun Proyecto.\n Pulsa el botón + para añadir un nuevo proyecto",
    onAddIconClicked:() -> Unit
){
    Column(modifier = Modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Proyectos",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 12.dp)
            )
            IconButton(onClick = onAddIconClicked) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Añadir Proyecto"
                )
            }
        }
        if(projectList.isEmpty()){
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(R.drawable.project_icon),
                contentDescription = emptyListText
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = emptyListText,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )

        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
        ){
            items(projectList) { projectList ->
                ProjectCard(
                    projectname = projectList.name,
                    gradientColors = projectList.colors,
                    onClick = {}
                )
            }
        }
    }
}