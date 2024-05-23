package com.example.newagilityapp.activites.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newagilityapp.activites.components.CountCard
import com.example.newagilityapp.activites.components.DeleteDialog
import com.example.newagilityapp.activites.components.NewProjectDialog
import com.example.newagilityapp.activites.components.ProjectCard

import com.example.newagilityapp.activites.components.projectSessionsList
import com.example.newagilityapp.activites.components.taskList

import com.example.newagilityapp.R
import com.example.newagilityapp.data.repository.ProjectRepository
import com.example.newagilityapp.data.repository.SessionRepository
import com.example.newagilityapp.data.repository.TaskRepository
import com.example.newagilityapp.data.viewmodels.ProjectViewModel
import com.example.newagilityapp.data.viewmodels.SessionViewModel
import com.example.newagilityapp.data.viewmodels.TaskViewModel
import com.example.newagilityapp.model.Project
import com.example.newagilityapp.model.Screens

import com.example.newagilityapp.projects
import com.example.newagilityapp.sesions
import com.example.newagilityapp.tasks
import kotlinx.coroutines.launch


@Composable
fun DashboardScreen(
    navigationController: NavController,
    drawerState: DrawerState,
    projectViewModel: ProjectViewModel,
    sessionViewModel: SessionViewModel,
    taskViewModel: TaskViewModel
) {
    val scope = rememberCoroutineScope()
    var isOpenNewProject by rememberSaveable { mutableStateOf(false) }
    var isOpenDelete by rememberSaveable { mutableStateOf(false) }
    var projectName by remember { mutableStateOf("") }
    var goalHours by remember { mutableStateOf("") }

    NewProjectDialog(
        isOpen = isOpenNewProject,
        title = "Añadir Projecto",
        projectname = projectName,
        goalHours = goalHours,
        onProjectNameChange = { projectName = it },
        onGoalHoursChange = { goalHours = it },
        onDismissRequest = { isOpenNewProject = false },
        onConfirmButtonsClick = {
            isOpenNewProject = false
            val project = Project(
                name = projectName,
                endDate = "", // Asegúrate de manejar esta fecha adecuadamente
                goalHours = goalHours.toFloatOrNull() ?: 0f
            )
            projectViewModel.addOrUpdateProject(project)
        }
    )

    DeleteDialog(
        isOpen = isOpenDelete,
        title = "Borrar sesión",
        text = "Vas a borrar una sessión de un proyecto \n " +
                "¿Estas seguro de hacerlo? Recuerda que esto no se puede revertir una vez hecho",
        onDismissRequest = { isOpenDelete = false },
        onConfirmButtonsClick = { isOpenDelete = false }
    )

    Scaffold(
        topBar = { DashboardScreenTopBar { scope.launch { drawerState.open() } } },
    ) { paddingValues ->
        val allProjects by projectViewModel.getAllProjects.collectAsState(initial = emptyList())
        val allSessions by sessionViewModel.allSessions.collectAsState(initial = emptyList())

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            item {
                CountCardSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    nproyectos = allProjects.size,
                    fproyectos = 3, // Aquí necesitas obtener la cantidad de proyectos finalizados
                    hours = 12 // Aquí necesitas obtener la cantidad de horas trabajadas
                )
            }
            item {
                ProjectsCardSection(
                    modifier = Modifier.fillMaxWidth(),
                    projectList = allProjects,
                    onAddIconClicked = { isOpenNewProject = true },
                    onProjectCardClick = { projectId ->
                        navigationController.navigate(Screens.ProjectScreen.route + "/$projectId")
                    }
                )
            }
            item {
                Button(
                    onClick = { navigationController.navigate(Screens.SessionScreen.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp, vertical = 20.dp)
                ) {
                    Text(text = "Iniciar sesión de trabajo")
                }
            }
            taskList(
                sectionTitle = "Trabajos por hacer",
                emptyListText = "No tienes trabajos por hacer. \n Pulsa el botón + para crear una nueva",
                tasks = emptyList(), // Aquí necesitas obtener la lista de tareas pendientes
                onCheckBoxClick = {},
                onTaskCardClick = { navigationController.navigate(Screens.TaskScreen.route) }
            )
            item {
                Spacer(modifier = Modifier.size(20.dp))
            }
            projectSessionsList(
                sectionTitle = "Sesiones de los proyectos",
                emptyListText = "No tienes ninguna sesión de Proyectos.\n !Añade una ahora¡",
                sessions = allSessions,
                onDeleteIconClick = { isOpenDelete = true }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardScreenTopBar(
    onClickDrawer: () -> Unit
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = { onClickDrawer() }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu"
                )
            }
        },
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
) {
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
    emptyListText: String = "No tienes ningun Proyecto.\n Pulsa el botón + para añadir un nuevo proyecto",
    onAddIconClicked: () -> Unit,
    onProjectCardClick: (Int?) -> Unit
) {
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
        if (projectList.isEmpty()) {
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
        ) {
            items(projectList) { project ->
                ProjectCard(
                    projectname = project.name,
                    onClick = { onProjectCardClick(project.projectId) }
                )
            }
        }
    }
}
