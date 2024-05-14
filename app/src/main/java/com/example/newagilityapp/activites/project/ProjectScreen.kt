package com.example.newagilityapp.activites.project

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
import com.example.newagilityapp.activites.components.CountCard
import com.example.newagilityapp.activites.components.DeleteDialog
import com.example.newagilityapp.activites.components.NewProjectDialog
import com.example.newagilityapp.activites.components.projectSessionsList
import com.example.newagilityapp.activites.components.taskList
import com.example.newagilityapp.activites.destinations.TaskScreenRouteDestination
import com.example.newagilityapp.activites.task.TaskScreenNavArgs
import com.example.newagilityapp.model.Project
import com.example.newagilityapp.sesions
import com.example.newagilityapp.tasks
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

data class ProjectScreenNavArgs(
    val projectId: Int
)

@Destination(navArgsDelegate = ProjectScreenNavArgs::class)
@Composable
fun ProjectScreenRoute(
    navigator: DestinationsNavigator
){
    ProjectScreen(
        onBackClick = {navigator.navigateUp()},
        onAddTaskClick = {
            val navArg = TaskScreenNavArgs(taskId = null,projectId = -1)
            navigator.navigate(TaskScreenRouteDestination(navArgs = navArg))
        },
        onTaskCard = {taskId ->
            val navArg = TaskScreenNavArgs(taskId = taskId,projectId = null)
            navigator.navigate(TaskScreenRouteDestination(navArgs = navArg))
        },
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreen(
    onBackClick: () -> Unit,
    onAddTaskClick: () -> Unit,
    onTaskCard: (Int?) -> Unit,
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val listState = rememberLazyListState()
    val isFABExpanded by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }

    var isOpenEditProject by rememberSaveable { mutableStateOf(false) }
    var isOpenDeleteProject by rememberSaveable { mutableStateOf(false) }
    var isOpenDeleteSession by rememberSaveable { mutableStateOf(false) }

    var projectName by remember { mutableStateOf("") }
    var goalHours by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(Project.CardColors.random()) }

    NewProjectDialog(
        isOpen = isOpenEditProject,
        title = "Añadir Projecto",
        selectedColors = selectedColor,
        projectname = projectName,
        goalHours = goalHours,
        onColorChange = {selectedColor = it},
        onProjectNameChange = {projectName = it},
        onGoalHoursChange = {goalHours = it},
        onDismissRequest = { isOpenEditProject = false },
        onConfirmButtonsClick = {
            isOpenEditProject = false
        },

        )

    DeleteDialog(
        isOpen = isOpenDeleteProject,
        title = "¿Borrar el proyecto?",
        text = "Vas a borrar un proyecto \n " +
                "¿Estas seguro de hacerlo? Recuerda que esto no se puede revertir una vez hecho",
        onDismissRequest = { isOpenDeleteProject = false },
        onConfirmButtonsClick = {isOpenDeleteProject = false}
    )

    DeleteDialog(
        isOpen = isOpenDeleteSession,
        title = "¿Borrar la sesión?",
        text = "Vas a borrar una sessión de un proyecto \n " +
                "¿Estas seguro de hacerlo? Recuerda que esto no se puede revertir una vez hecho",
        onDismissRequest = { isOpenDeleteSession = false },
        onConfirmButtonsClick = {isOpenDeleteSession = false}
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { ProjectScreenTopBar(
            title = "Aldi",
            onBackClick = onBackClick,
            onDeleteClick = {isOpenDeleteProject = true},
            onEditClick = {isOpenEditProject = true},
            scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = "Añadir Trabajo") },
                icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir")},
                onClick = onAddTaskClick,
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
                onTaskCardClick = onTaskCard
            )
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            taskList(
                sectionTitle = "Trabajos Completados",
                emptyListText = "No tienes trabajos completados. \n Pulsa el botón + para crear una nueva",
                tasks = tasks,
                onCheckBoxClick = {},
                onTaskCardClick = onTaskCard
            )
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            projectSessionsList(
                sectionTitle = "Sesiones de los proyectos",
                emptyListText = "No tienes ninguna sesión de Proyectos.\n !Añade una ahora¡",
                sessions = sesions,
                onDeleteIconClick = {isOpenDeleteSession = true}
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
    onEditClick:() -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
){
    LargeTopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
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