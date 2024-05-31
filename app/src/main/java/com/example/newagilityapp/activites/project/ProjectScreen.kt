package com.example.newagilityapp.activites.project

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.newagilityapp.activites.components.CountCard
import com.example.newagilityapp.activites.components.DeleteDialog
import com.example.newagilityapp.activites.components.ProjectSessions
import com.example.newagilityapp.activites.components.taskList
import com.example.newagilityapp.data.viewmodels.ProjectViewModel
import com.example.newagilityapp.data.viewmodels.SessionViewModel
import com.example.newagilityapp.data.viewmodels.TaskViewModel
import com.example.newagilityapp.model.Screens
import com.example.newagilityapp.model.Session


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreen(
    navigationController: NavHostController,
    projectId: Int
) {
    val projectViewModel: ProjectViewModel = hiltViewModel()
    val taskViewModel: TaskViewModel = hiltViewModel()
    val sessionViewModel: SessionViewModel = hiltViewModel()


    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val listState = rememberLazyListState()
    val isFABExpanded by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }

    var isOpenEditProject by rememberSaveable { mutableStateOf(false) }
    var isOpenDeleteProject by rememberSaveable { mutableStateOf(false) }
    var isOpenDeleteSession by rememberSaveable { mutableStateOf(false) }


    val project by projectViewModel.getProjectById(projectId).collectAsState(initial = null)
    val totalTasks by taskViewModel.getTotalTasks(projectId).collectAsState(initial = 0)
    val completedTasks by taskViewModel.getCompletedTasks(projectId).collectAsState(initial = 0)
    val hoursDone by projectViewModel.getTotalHoursFromSessions(projectId).collectAsState(initial = null)
    val sessions by sessionViewModel.getSessionByProjectId(projectId).collectAsState(initial = emptyList())
    val tasks by taskViewModel.getTaskByProjectId(projectId).collectAsState(initial = emptyList())
    val progress = if (totalTasks > 0) completedTasks / totalTasks.toFloat() else 0f

    var sessionToDelete by rememberSaveable { mutableStateOf<Session?>(null) }

    DeleteDialog(
        isOpen = isOpenDeleteProject,
        title = "¿Borrar el proyecto?",
        text = "Vas a borrar un proyecto \n " +
                "¿Estas seguro de hacerlo? Recuerda que esto no se puede revertir una vez hecho",
        onDismissRequest = { isOpenDeleteProject = false },
        onConfirmButtonsClick = {
            isOpenDeleteProject = false
            projectViewModel.deleteProject(projectId)
        }
    )

    DeleteDialog(
        isOpen = isOpenDeleteSession,
        title = "¿Borrar la sesión?",
        text = "Vas a borrar una sessión de un proyecto \n " +
                "¿Estas seguro de hacerlo? Recuerda que esto no se puede revertir una vez hecho",
        onDismissRequest = { isOpenDeleteSession = false },
        onConfirmButtonsClick = {
            isOpenDeleteSession = false
            sessionToDelete?.let { sessionViewModel.deleteSession(it) }
        }
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            project?.let {
                ProjectScreenTopBar(
                    title = it.name,
                    navigationController = navigationController,
                    onDeleteClick = {isOpenDeleteProject = true},
                    onEditClick = {
                        isOpenEditProject = true
                        navigationController.navigate(Screens.NewProjectScreen.route)
                                  },
                    scrollBehavior = scrollBehavior
                )
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = "Añadir Trabajo") },
                icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir")},
                onClick = {navigationController.navigate(Screens.TaskScreen.route)},
                expanded = isFABExpanded
            )
        }
    ) {paddingValue ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ){
            item {
                ProjectOverviewSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    hoursDone = hoursDone.toString(),
                    goalHours = project?.goalHours.toString(),
                    progress = progress
                )
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            taskList(
                sectionTitle = "Trabajos por hacer",
                emptyListText = "No tienes por hacer",
                tasks = tasks.filter { !it.isDone },
                onEmptyClick = {navigationController.navigate(Screens.TaskScreen.route)},
                onCheckBoxClick = {task -> taskViewModel.addOrUpdateTask(task.copy(isDone = true)) },
                onTaskCardClick = {navigationController.navigate(Screens.TaskScreen.route)}
            )
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            taskList(
                sectionTitle = "Trabajos Completados",
                emptyListText = "No tienes trabajos Completados",
                tasks = tasks.filter { it.isDone },
                onEmptyClick = {navigationController.navigate(Screens.TaskScreen.route)},
                onCheckBoxClick = {task -> taskViewModel.addOrUpdateTask(task.copy(isDone = false)) },
                onTaskCardClick = {navigationController.navigate(Screens.TaskScreen.route)}
            )
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            ProjectSessions(
                sectionTitle = "Sesiones de los proyectos",
                emptyListText = "No tienes ninguna sesión de Proyectos.\n !Añade una ahora¡",
                sessions = sessions,
                onDeleteIconClick = {
                    isOpenDeleteSession = true
                    sessionToDelete = it
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProjectScreenTopBar(
    navigationController: NavHostController,
    title: String,
    onDeleteClick:() -> Unit,
    onEditClick:() -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
){
    LargeTopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = { navigationController.popBackStack() }) {
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
            IconButton(onClick = { onDeleteClick() }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Borrar Proyecto"
                )
            }
            IconButton(onClick = { onEditClick() }) {
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
                progress = { 1f },
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surfaceVariant,
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round,
            )
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxSize(),
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round,
            )
            Text(text = "$percentage%")
        }
    }
}