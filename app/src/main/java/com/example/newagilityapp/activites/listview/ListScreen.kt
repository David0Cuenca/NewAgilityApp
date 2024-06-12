package com.example.newagilityapp.activites.listview

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.newagilityapp.R
import com.example.newagilityapp.data.viewmodels.ProjectViewModel
import com.example.newagilityapp.data.viewmodels.TaskViewModel
import com.example.newagilityapp.model.Project
import com.example.newagilityapp.model.Screens
import com.example.newagilityapp.utilities.formatDuration
import com.example.newagilityapp.utilities.formatGoalHours
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    navigationController: NavHostController,
    drawerState: DrawerState,
    ) {
    val projectViewModel: ProjectViewModel = hiltViewModel()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val listState = rememberLazyListState()
    
    val projects by projectViewModel.getAllProjects.collectAsState(initial = emptyList())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ListScreenTopBar(
                title = "Lista de proyectos",
                drawerState,
                scrollBehavior = scrollBehavior
            )
        },

        ) { paddingValue ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue),
            verticalArrangement = Arrangement.Center
        ) {
            item {
                ListProjects(
                    projects = projects,
                    projectViewModel = projectViewModel,
                    emptyListText = "No hay ningun Projecto creado",
                    onProjectCardClick = { projectId ->
                        navigationController.navigate(Screens.ProjectScreen.createRoute(projectId))
                    }
                )
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun ListProjects(
    projects: List<Project>,
    emptyListText: String,
    projectViewModel: ProjectViewModel,
    onProjectCardClick: (Int) -> Unit
) {
    val taskViewModel: TaskViewModel = hiltViewModel()
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (projects.isEmpty()) {
            var offset by remember { mutableFloatStateOf(0f) }
            val animatedOffset by animateFloatAsState(
                targetValue = if (offset == 0f) 15f else 0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 2000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "",
            )
            LaunchedEffect(Unit) {
                while (true) {
                    offset = if (offset == 0f) 15f else 0f
                    delay(2000)
                }
            }
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .offset(y = animatedOffset.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.empty),
                    contentDescription = "Empty",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(180.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = emptyListText,
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center
            )
        }
        projects.forEach { project ->
            val formattedGoalHours = formatGoalHours(project.goalHours)
            val hoursDone by projectViewModel.getTotalHoursFromSessions(project.projectId!!).collectAsState(initial = 0L)
            val completedTasks by taskViewModel.getCompletedTasks(project.projectId!!)
                .collectAsState(initial = 0)
            val totalTasks by taskViewModel.getTotalTasks(project.projectId!!)
                .collectAsState(initial = 0)
            val progress = if (totalTasks > 0) completedTasks / totalTasks.toFloat() else 0f
            projectCard(
                projectname = project.name,
                goalHours = formattedGoalHours,
                hoursDone = hoursDone,
                endDate = project.endDate,
                progress = progress,
                onClick = { onProjectCardClick(project.projectId!!) }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun projectCard(
    projectname: String,
    endDate: String,
    progress: Float,
    onClick: () -> Unit,
    goalHours: String,
    hoursDone: Long
) {
    val parsedGoalHours = goalHours.toFloatOrNull() ?: 0f
    val formattedGoalHours = formatGoalHours(parsedGoalHours)
    val formattedHoursDone = formatDuration(hoursDone)
    val percentage = remember(progress) {
        (progress * 100).toInt().coerceIn(0, 100)
    }
    ElevatedCard(
        modifier = Modifier
            .clickable { onClick() }
    ) {
        Row(
            Modifier
                .padding(10.dp)
                .fillMaxWidth(),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = projectname,
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Icon(
                        painter = painterResource(R.drawable.project_icon),
                        contentDescription = "Icon"
                    )
                }
                Spacer(modifier = Modifier.size(10.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Fecha fin:",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = endDate,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        Text(
                            text = "Horas hechas"
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = formattedHoursDone,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Horas objetivos",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = endDate,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        Text(
                            text = "Horas objetivos:",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = formattedGoalHours,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                
                Box(
                    modifier = Modifier.size(75.dp),
                    contentAlignment = Alignment.Center
                ) {
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListScreenTopBar(
    title: String,
    drawerState: DrawerState,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = { scope.launch { drawerState.open() } }
            ) {
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
