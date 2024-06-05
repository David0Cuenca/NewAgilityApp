package com.example.newagilityapp.activites.project

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.newagilityapp.R
import com.example.newagilityapp.activites.components.CountCard
import com.example.newagilityapp.activites.components.Alert
import com.example.newagilityapp.activites.components.LoadingAnimationDialog
import com.example.newagilityapp.activites.components.ProjectSessions
import com.example.newagilityapp.activites.components.TaskDatePicker
import com.example.newagilityapp.activites.components.TimePickerDialog
import com.example.newagilityapp.activites.components.taskList
import com.example.newagilityapp.data.viewmodels.ProjectViewModel
import com.example.newagilityapp.data.viewmodels.SessionViewModel
import com.example.newagilityapp.data.viewmodels.TaskViewModel
import com.example.newagilityapp.model.Project
import com.example.newagilityapp.model.Screens
import com.example.newagilityapp.model.Session
import com.example.newagilityapp.utilities.changeMillisToDateString
import com.example.newagilityapp.utilities.daysLeft
import com.example.newagilityapp.utilities.formatDuration
import com.example.newagilityapp.utilities.formatGoalHours
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

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

    var isOpenDeleteProject by rememberSaveable { mutableStateOf(false) }
    var isOpenDeleteSession by rememberSaveable { mutableStateOf(false) }
    var showLoadingDialog by remember { mutableStateOf(false) }

    val project by projectViewModel.getProjectById(projectId).collectAsState(initial = null)
    val totalTasks by taskViewModel.getTotalTasks(projectId).collectAsState(initial = 0)
    val completedTasks by taskViewModel.getCompletedTasks(projectId).collectAsState(initial = 0)
    val hoursDone by projectViewModel.getTotalHoursFromSessions(projectId).collectAsState(initial = 0L)
    val sessions by sessionViewModel.getSessionByProjectId(projectId).collectAsState(initial = emptyList())
    val tasks by taskViewModel.getTaskByProjectId(projectId).collectAsState(initial = emptyList())
    val progress = if (totalTasks > 0) completedTasks / totalTasks.toFloat() else 0f

    val scope = rememberCoroutineScope()

    var isEditing by rememberSaveable { mutableStateOf(false) }
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var goalHoursAndMinutes by rememberSaveable { mutableStateOf("1:00") }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = Instant.now().toEpochMilli())
    var isDatePickerOpen by rememberSaveable { mutableStateOf(false) }
    var isTimePickerOpen by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(project) {
        project?.let {
            title = it.name
            description = it.description
            goalHoursAndMinutes = String.format(
                Locale.getDefault(),
                "%02d:%02d",
                it.goalHours.toInt(),
                ((it.goalHours - it.goalHours.toInt()) * 60).toInt()
            )
        }
    }

    var sessionToDelete by rememberSaveable { mutableStateOf<Session?>(null) }

    if (showLoadingDialog) {
        LoadingAnimationDialog(
            onDismiss = {
                showLoadingDialog = false
                navigationController.popBackStack()
            }
        )
    }
    Alert(
        isOpen = isOpenDeleteProject,
        title = "¿Borrar el proyecto?",
        text = "Vas a borrar un proyecto \n " +
                "¿Estas seguro de hacerlo? Recuerda que esto no se puede revertir una vez hecho",
        onDismissRequest = { isOpenDeleteProject = false },
        onConfirmButtonsClick = {
            isOpenDeleteProject = false
            projectViewModel.deleteProject(projectId)
            showLoadingDialog = true
        }
    )
    Alert(
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
                    isEditing = isEditing,
                    onDeleteClick = { isOpenDeleteProject = true },
                    onEditClick = { isEditing = true },
                    onSaveClick = {
                        isEditing = false
                        val hoursMinutesArray = goalHoursAndMinutes.split(":").map { it.toInt() }
                        val goalHours = hoursMinutesArray[0].toFloat() + (hoursMinutesArray[1].toFloat() / 60)
                        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                        val formattedDate = Instant.ofEpochMilli(datePickerState.selectedDateMillis!!)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                            .format(formatter)

                        val updatedProject = Project(
                            projectId = projectId,
                            name = title,
                            endDate = formattedDate,
                            goalHours = goalHours,
                            description = description
                        )

                        scope.launch {
                            showLoadingDialog = true
                            projectViewModel.updateProject(updatedProject)
                        }
                    },
                    onCancelClick = { isEditing = false },
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
                if (isEditing) {
                    EditableProjectDetailsSection(
                        title = title,
                        onTitleChange = { title = it },
                        description = description,
                        onDescriptionChange = { description = it },
                        goalHoursAndMinutes = goalHoursAndMinutes,
                        onGoalHoursAndMinutesChange = { goalHoursAndMinutes = it },
                        datePickerState = datePickerState,
                        onDateClick = { isDatePickerOpen = true },
                        isDatePickerOpen = isDatePickerOpen,
                        onDatePickerDismiss = { isDatePickerOpen = false },
                        onDatePickerConfirm = { isDatePickerOpen = false },
                        isTimePickerOpen = isTimePickerOpen,
                        onTimePickerDismiss = { isTimePickerOpen = false },
                        onTimePickerOpen = {isTimePickerOpen = true},
                        onTimeSelected = { hours, minutes ->
                            goalHoursAndMinutes = String.format(Locale.getDefault(), "%02d:%02d", hours, minutes)
                            isTimePickerOpen = false
                        }
                    )
                } else {
                    ProjectOverviewSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        hoursDone = hoursDone,
                        goalHours = project?.goalHours.toString(),
                        progress = progress,
                        endDate = project?.endDate
                    )
                    Column (Modifier.padding(12.dp)){
                        Text(text = "Descripcion", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(description, style = MaterialTheme.typography.bodyMedium)
                    }
                }
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
    isEditing: Boolean,
    onDeleteClick:() -> Unit,
    onEditClick:() -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
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
            if (isEditing) {
                IconButton(onClick = onSaveClick) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Guardar Cambios"
                    )
                }
                IconButton(onClick = onCancelClick) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancelar Edición"
                    )
                }
            } else {
                IconButton(onClick = onDeleteClick ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Borrar Proyecto"
                    )
                }
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar Proyecto"
                    )
                }
            }
        }
    )
}


@Composable
private fun ProjectOverviewSection(
    modifier: Modifier,
    hoursDone: Long,
    goalHours: String,
    progress: Float,
    endDate: String?,
) {
    val parsedGoalHours = goalHours.toFloatOrNull() ?: 0f
    val formattedGoalHours = formatGoalHours(parsedGoalHours)
    val formattedHoursDone = formatDuration(hoursDone)

    val percentage = remember(progress) {
        (progress * 100).toInt().coerceIn(0, 100)
    }
    val (_, formattedRemainingTime) = daysLeft(endDate)
    Column(
        Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CountCard(
                modifier = Modifier.weight(1f),
                title = "Objetivo de Horas",
                count = formattedGoalHours
            )
            Spacer(modifier = Modifier.width(10.dp))
            CountCard(
                modifier = Modifier.weight(1f),
                title = "Tiempo hecho",
                count = formattedHoursDone
            )
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(80.dp),
                    progress = { 1f },
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    strokeWidth = 4.dp,
                    strokeCap = StrokeCap.Round,
                )
                CircularProgressIndicator(
                    modifier = Modifier.size(80.dp),
                    progress = { progress },
                    strokeWidth = 4.dp,
                    strokeCap = StrokeCap.Round,
                )
                Text(text = "$percentage%")
            }
        }
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CountCard(
                modifier = Modifier.weight(1f),
                title = "Fecha final",
                count = "$endDate"
            )
            Spacer(modifier = Modifier.width(10.dp))
            CountCard(
                modifier = Modifier.weight(1f),
                title = "Tiempo restante",
                count = formattedRemainingTime
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditableProjectDetailsSection(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    goalHoursAndMinutes: String,
    onGoalHoursAndMinutesChange: (String) -> Unit,
    datePickerState: DatePickerState,
    onDateClick: () -> Unit,
    isDatePickerOpen: Boolean,
    onDatePickerDismiss: () -> Unit,
    onDatePickerConfirm: () -> Unit,
    isTimePickerOpen: Boolean,
    onTimePickerOpen: (Boolean) -> Unit,
    onTimePickerDismiss: () -> Unit,
    onTimeSelected: (Int, Int) -> Unit
) {
    Column(modifier = Modifier.padding(12.dp)) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = title,
            onValueChange = onTitleChange,
            label = { Text(text = "Título") },
            singleLine = true
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text(text = "Descripción") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Fecha de entrega",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedBox(
                    value = datePickerState.selectedDateMillis.changeMillisToDateString(),
                    onClick = onDateClick,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Selecciona fecha final"
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Objetivo de horas",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedBox(
                    modifier = Modifier.fillMaxWidth(),
                    value = goalHoursAndMinutes,
                    onClick = { onTimePickerOpen(true) },
                    trailingIcon = {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.session_icon),
                            contentDescription = "Selecciona tiempo objetivo"
                        )
                    }
                )
            }
        }

        TaskDatePicker(
            state = datePickerState,
            isOpen = isDatePickerOpen,
            onDismissRequest = onDatePickerDismiss,
            onConfirmButtonClick = onDatePickerConfirm
        )

        if (isTimePickerOpen) {
            TimePickerDialog(
                initialHours = goalHoursAndMinutes.split(":")[0].toInt(),
                initialMinutes = goalHoursAndMinutes.split(":")[1].toInt(),
                onTimeSelected = onTimeSelected,
                onDismiss = onTimePickerDismiss
            )
        }
    }
}