package com.example.newagilityapp.activites.calendar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.newagilityapp.R
import com.example.newagilityapp.activites.components.FilterBar
import com.example.newagilityapp.activites.components.FilterType
import com.example.newagilityapp.data.viewmodels.ProjectViewModel
import com.example.newagilityapp.data.viewmodels.TaskViewModel
import com.example.newagilityapp.model.Project
import com.example.newagilityapp.model.Screens
import com.example.newagilityapp.model.Task
import com.example.newagilityapp.utilities.displayText
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.daysOfWeek
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    navigationController: NavHostController,
    drawerState: DrawerState,
) {
    val projectViewModel: ProjectViewModel = hiltViewModel()
    val taskViewModel:TaskViewModel = hiltViewModel()

    val daysOfWeek = remember { daysOfWeek() }
    val currentMonth = remember { YearMonth.now() }
    var actualMonth by rememberSaveable { mutableStateOf(currentMonth) }
    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(100) }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scope = rememberCoroutineScope()
    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
    )

    val projects by projectViewModel.getAllProjects.collectAsState(initial = emptyList())
    val tasks by taskViewModel.getAllTasks.collectAsState(initial = emptyList())

    val projectEndDates by remember(projects) {
        derivedStateOf {
            projects.map { LocalDate.parse(it.endDate, DateTimeFormatter.ofPattern("dd MMM yyyy")) }
        }
    }
    val taskEndDates by remember(tasks) {
        derivedStateOf {
            tasks.map { LocalDate.parse(it.endate, DateTimeFormatter.ofPattern("dd MMM yyyy")) }
        }
    }
    var selectedFilter by rememberSaveable { mutableStateOf(FilterType.Tasks) }
    var selectedDate by rememberSaveable { mutableStateOf<LocalDate?>(null) }
    val visibleState = remember { MutableTransitionState(false) }

    LaunchedEffect(actualMonth) {
        selectedDate = null
        visibleState.targetState = false
    }

    Scaffold(
        Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CalendarScreenTopBar(
                title = "Calendario",
                drawerState = drawerState,
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FilterBar(
                selectedFilter = selectedFilter,
                onFilterSelected = {selectedFilter = it}
            )
            CalendarTitle(
                actualMonth,
                daysOfWeek,
                goNext = {
                    scope.launch {
                        actualMonth = actualMonth.plusMonths(1)
                        state.animateScrollToMonth(actualMonth)
                    }
                },
                goBack = {
                    scope.launch {
                        actualMonth = actualMonth.minusMonths(1)
                        state.animateScrollToMonth(actualMonth)
                    }
                }
            )
            HorizontalCalendar(
                state = state,
                dayContent = { day ->
                    Day(
                        day = day,
                        projectEndDates = projectEndDates,
                        taskEndDates = taskEndDates,
                        selectedFilter = selectedFilter,
                        onClick = {
                            selectedDate = day.date
                            visibleState.targetState = true
                        }
                    )
                },
                userScrollEnabled = false
            )
            AnimatedVisibility(
                visibleState = visibleState,
                enter = expandVertically(animationSpec = tween(durationMillis = 300)) + fadeIn(animationSpec = tween(durationMillis = 300)),
                exit = shrinkVertically(animationSpec = tween(durationMillis = 300)) + fadeOut(animationSpec = tween(durationMillis = 300))
            ) {
                selectedDate?.let { date ->
                    when (selectedFilter) {
                        FilterType.Tasks -> TaskSummary(date, tasks, navigationController)
                        FilterType.Projects -> ProjectSummary(date, projects, navigationController)
                        FilterType.Both -> {
                            Column {
                                TaskSummary(date, tasks, navigationController)
                                ProjectSummary(date, projects, navigationController)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarTitle(
    currentMonth: YearMonth,
    daysOfWeek: List<DayOfWeek>,
    goNext: () -> Unit,
    goBack: () -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CalendarNavigationIcon(
            Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Mes anterior",
            onClick = goBack
        )
        Text(
            text = currentMonth.displayText().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
            style = MaterialTheme.typography.titleLarge
        )
        CalendarNavigationIcon(
            Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Mes siguiente",
            onClick = goNext,
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
    DaysOfWeekTitle(daysOfWeek = daysOfWeek)
}

@Composable
fun Day(
    day: CalendarDay,
    projectEndDates: List<LocalDate>,
    taskEndDates: List<LocalDate>,
    selectedFilter: FilterType,
    onClick: (CalendarDay) -> Unit
) {
    val isProjectEndDate = projectEndDates.contains(day.date)
    val isTaskEndDate = taskEndDates.contains(day.date)
    val showIcon = when (selectedFilter) {
        FilterType.Tasks -> isTaskEndDate
        FilterType.Projects -> isProjectEndDate
        FilterType.Both -> isProjectEndDate || isTaskEndDate
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .clickable { onClick(day) },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = day.date.dayOfMonth.toString())
            if (showIcon) {
                Icon(imageVector = Icons.Default.Info, contentDescription = "Evento")
            }
        }
    }
}
@Composable
fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("es", "Es")),
            )
        }
    }
}

@Composable
private fun CalendarNavigationIcon(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) = Box(
    modifier = Modifier
        .clip(shape = CircleShape)
        .clickable(role = Role.Button, onClick = onClick),
) {
    Icon(
        modifier = Modifier
            .padding(4.dp)
            .align(Alignment.Center),
        imageVector = imageVector,
        contentDescription = contentDescription,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalendarScreenTopBar(
    title: String,
    drawerState: DrawerState,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val scope = rememberCoroutineScope()
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu"
                )
            }
        },
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineLarge
            )
        },
    )
}
@Composable
fun TaskSummary(
    selectedDate: LocalDate,
    tasks: List<Task>,
    navigationController: NavHostController
) {
    val tasksForDate = tasks.filter { LocalDate.parse(it.endate, DateTimeFormatter.ofPattern("dd MMM yyyy")) == selectedDate }
    if (tasksForDate.isNotEmpty()) {
        Column(Modifier.padding(10.dp)) {
            Text(
                text = "Resumen de Tareas",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(10.dp))

            tasksForDate.forEach { task ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navigationController.navigate(Screens.ProjectScreen.createRoute(task.taskProjectId))
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                bottomEndPercent = 0,
                                bottomStartPercent = 10,
                                topEndPercent = 0,
                                topStartPercent = 10
                            )
                        )
                        .background(color = MaterialTheme.colorScheme.onPrimary)
                        .size(50.dp),
                        Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(40.dp),
                            painter = painterResource(R.drawable.project_icon),
                            contentDescription = "Icono"
                        )
                    }
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .clip(
                                RoundedCornerShape(
                                    bottomEndPercent = 10,
                                    bottomStartPercent = 0,
                                    topEndPercent = 10,
                                    topStartPercent = 0
                                )
                            )
                            .background(
                                brush = Brush.horizontalGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.onPrimary,
                                        MaterialTheme.colorScheme.background
                                    )
                                )
                            )
                    ) {
                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "Fecha de entrega: ${task.endate}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}
@Composable
fun ProjectSummary(
    selectedDate: LocalDate,
    projects: List<Project>,
    navigationController: NavHostController
) {
    val projectsForDate = projects.filter { LocalDate.parse(it.endDate, DateTimeFormatter.ofPattern("dd MMM yyyy")) == selectedDate }

    if (projectsForDate.isNotEmpty()) {
        Column(Modifier.padding(10.dp)) {
            Text(
                text = "Resumen de Proyectos",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(10.dp))
                projectsForDate.forEach { project ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navigationController.navigate(Screens.ProjectScreen.createRoute(project.projectId!!))
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier
                            .clip(
                                RoundedCornerShape(
                                    bottomEndPercent = 0,
                                    bottomStartPercent = 20,
                                    topEndPercent = 0,
                                    topStartPercent = 20
                                )
                            )
                            .background(color = MaterialTheme.colorScheme.onSecondary)
                            .size(50.dp),
                            Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(40.dp),
                                painter = painterResource(R.drawable.project_icon),
                                contentDescription = "Icono"
                            )
                        }
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .clip(
                                    RoundedCornerShape(
                                        bottomEndPercent = 20,
                                        bottomStartPercent = 0,
                                        topEndPercent = 20,
                                        topStartPercent = 0
                                    )
                                )
                                .background(
                                    brush = Brush.horizontalGradient(
                                        listOf(
                                            MaterialTheme.colorScheme.onSecondary,
                                            MaterialTheme.colorScheme.background
                                        )
                                    )
                                )
                        ) {
                            Text(
                                text = project.name,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "Fecha de entrega: ${project.endDate}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
        }
    }
}
