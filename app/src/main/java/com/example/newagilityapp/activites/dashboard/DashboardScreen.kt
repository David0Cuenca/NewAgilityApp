package com.example.newagilityapp.activites.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Add
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.newagilityapp.R
import com.example.newagilityapp.activites.components.dialogs.Alert
import com.example.newagilityapp.activites.components.uielements.CountCard
import com.example.newagilityapp.activites.components.uielements.DailyReport
import com.example.newagilityapp.activites.components.uielements.ProjectCard
import com.example.newagilityapp.activites.components.uielements.projectSessions
import com.example.newagilityapp.activites.components.uielements.taskList
import com.example.newagilityapp.data.viewmodels.ProjectViewModel
import com.example.newagilityapp.data.viewmodels.SessionViewModel
import com.example.newagilityapp.data.viewmodels.TaskViewModel
import com.example.newagilityapp.model.Project
import com.example.newagilityapp.model.Screens
import com.example.newagilityapp.model.Session
import com.example.newagilityapp.utilities.formatDuration
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen(
    navigationController: NavController,
    drawerState: DrawerState,
) {
    val projectViewModel: ProjectViewModel = hiltViewModel()
    val sessionViewModel: SessionViewModel = hiltViewModel()
    val taskViewModel: TaskViewModel = hiltViewModel()

    val scope = rememberCoroutineScope()

    var isOpenDelete by rememberSaveable { mutableStateOf(false) }

    var sessionToDelete by remember { mutableStateOf<Session?>(null) }

    val allProjects by projectViewModel.getAllProjects.collectAsState(initial = emptyList())
    val allSessions by sessionViewModel.getAllSessions.collectAsState(initial = emptyList())
    val allTasks by taskViewModel.getAllTasks.collectAsState(initial = emptyList())
    val completedProjectsCount by projectViewModel.completedProjectsCount.observeAsState(0)
    val totalHoursWorked by projectViewModel.totalHoursWorked.observeAsState(0L)

        Alert(
            isOpen = isOpenDelete,
            title = "Borrar sesión",
            text = "Vas a borrar una sessión de un proyecto \n " +
                    "¿Estas seguro de hacerlo? Recuerda que esto no se puede revertir una vez hecho",
            onDismissRequest = { isOpenDelete = false },
            onConfirmButtonsClick = {
                isOpenDelete = false
                sessionToDelete?.let { sessionViewModel.deleteSession(it) }
            },
        )
    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            DashboardScreenTopBar {
                scope.launch { drawerState.open() }
            }
        },
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize(),
                contentScale = ContentScale.Crop
        )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
                item {
                    CountCardSection(
                        nproyectos = allProjects.size,
                        fproyectos = completedProjectsCount,
                        hours = totalHoursWorked
                    )
                }
                item {
                    ProjectsCardSection(
                        projectList = allProjects,
                        onAddIconClicked = { navigationController.navigate(Screens.NewProjectScreen.route) },
                        onProjectCardClick = { projectId ->
                            navigationController.navigate(
                                Screens.ProjectScreen.createRoute(projectId)
                            )
                        },
                        onViewAllClicked = { navigationController.navigate(Screens.ListScreen.route) }
                    )
                }
                item {
                 DailyReport(navigationController)
                }
                taskList(
                    sectionTitle = "Trabajos por hacer",
                    emptyListText = "No tienes trabajos. \n Pulsa para crear una nueva",
                    tasks = allTasks.filter { !it.isDone }.sortedWith(compareBy({it.priority},{it.endate})).take(6),
                    onEmptyClick = { navigationController.navigate(Screens.TaskScreen.route) },
                    onCheckBoxClick = { it.isDone = true },
                    onTaskCardClick = { navigationController.navigate(Screens.TaskScreen.route) }
                )
                item {
                    Spacer(modifier = Modifier.size(20.dp))
                }
                projectSessions(
                    sectionTitle = "Historial de sesiones",
                    emptyListText = "No tienes ninguna sesión de Proyectos.",
                    sessions = allSessions.sortedBy { it.date }.take(6),
                    onDeleteIconClick = {
                        isOpenDelete = true
                        sessionToDelete=it
                    }
                )
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
            }
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
            IconButton(onClick = onClickDrawer) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu"
                )
            }
        },
        title = {
            Icon(
                painter = painterResource(id = R.drawable.na_logo),
                contentDescription = "Logo",
                Modifier.size(200.dp)
            )
        }
    )
}

@Composable
private fun CountCardSection(
    nproyectos: Int,
    fproyectos: Int,
    hours: Long,
) {
    Row(horizontalArrangement = Arrangement.SpaceAround) {
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
            title = "Tiempo de Trabajo",
            count = formatDuration(hours)
        )
    }
}

@Composable
private fun ProjectsCardSection(
    projectList: List<Project>,
    emptyListText: String = "No tienes ningún Proyecto. \n Pulsa el botón + para añadir un nuevo proyecto",
    onAddIconClicked: () -> Unit,
    onProjectCardClick: (Int) -> Unit,
    onViewAllClicked: () -> Unit
) {
    Spacer(modifier = Modifier.height(10.dp))
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Proyectos",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(12.dp)
            )
            IconButton(onClick = onAddIconClicked) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Añadir Proyecto"
                )
            }
        }

        if (projectList.isEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            IconButton(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally),
                onClick = onAddIconClicked
            ) {
                Icon(
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.CenterHorizontally),
                    imageVector = Icons.Outlined.Add,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = emptyListText
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                text = emptyListText,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        } else {
            val displayedProjects = projectList.take(6)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                for (i in displayedProjects.indices step 2) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ProjectCard(
                            projectname = displayedProjects[i].name,
                            onClick = { displayedProjects[i].projectId?.let { onProjectCardClick(it) } },
                            modifier = Modifier.weight(1f)
                        )
                        if (i + 1 < displayedProjects.size) {
                            ProjectCard(
                                projectname = displayedProjects[i + 1].name,
                                onClick = { displayedProjects[i + 1].projectId?.let { onProjectCardClick(it) } },
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
                if (projectList.size > 6) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = onViewAllClicked,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(text = "Ver todos los proyectos")
                    }
                }
            }
        }
    }
}
