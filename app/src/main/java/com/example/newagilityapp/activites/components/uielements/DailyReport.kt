package com.example.newagilityapp.activites.components.uielements

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.newagilityapp.activites.components.notifications.showDailyReportNotification
import com.example.newagilityapp.data.viewmodels.ProjectViewModel
import com.example.newagilityapp.data.viewmodels.TaskViewModel
import com.example.newagilityapp.model.Project
import com.example.newagilityapp.model.Screens
import com.example.newagilityapp.model.Task
import com.example.newagilityapp.utilities.changeMillisToDateString
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyReport(navigationController: NavController) {
    val context = LocalContext.current
    val projectViewModel: ProjectViewModel = hiltViewModel()
    val taskViewModel: TaskViewModel = hiltViewModel()

    val allTasks by taskViewModel.getAllTasks.collectAsState(initial = emptyList())
    val allProjects by projectViewModel.getAllProjects.collectAsState(initial = emptyList())

    if (allTasks.isEmpty() && allProjects.isEmpty()) {
        EmptyStateScreen()
    } else {
        val todayProjects = allProjects.filter { it.endDate == Instant.now().toEpochMilli().changeMillisToDateString() }
        val todayTasks = allTasks.filter { it.endate == Instant.now().toEpochMilli().changeMillisToDateString() && !it.isDone }
        Report(todayTasks,todayProjects,projectViewModel,navigationController)
        showDailyReportNotification(context, todayTasks, todayProjects)
    }
}

@Composable
fun EmptyStateScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = "Reporte Semanal",
            style = MaterialTheme.typography.titleLarge
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Empty",
                modifier = Modifier.size(100.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No hay proyectos ni tareas disponibles",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun Report(
    allTasks: List<Task>,
    allProjects: List<Project>,
    projectViewModel: ProjectViewModel,
    navigationController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = "Reporte diario",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(10.dp))
        Column {
            if (allTasks.isNotEmpty()) {
                Text(
                    text = "Trabajos",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(5.dp))
                allTasks.forEach { task ->
                    TaskItem(task = task,projectViewModel,navigationController)
                    Spacer(modifier = Modifier.height(5.dp))
                }
            } else {
                Text(text = "No hay Tareas para hoy")
            }
            Spacer(modifier = Modifier.height(10.dp))

            if (allProjects.isNotEmpty()) {
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Projectos",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(5.dp))
                allProjects.forEach { project ->
                    ProjectItem(project = project,navigationController)
                    Spacer(modifier = Modifier.height(5.dp))
                }
            } else {
                Text(text = "No hay Proyectos para hoy")
            }
        }
    }
}
@Composable
fun TaskItem(task: Task, projectViewModel: ProjectViewModel, navigationController: NavController) {
    val name by projectViewModel.getProjectById(task.taskProjectId).collectAsState(initial = null)
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navigationController.navigate(Screens.ProjectScreen.createRoute(task.taskProjectId)) },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleSmall
            )
            Text(text = "Nombre del Proyecto: ${name?.name}")
            if (task.description.isNotEmpty()){
                Text(
                    text = "Descripción: ${task.description}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun ProjectItem(project: Project, navigationController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navigationController.navigate(Screens.ProjectScreen.createRoute(project.projectId!!)) },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = project.name,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = "Descripción: ${project.description.ifEmpty { "No hay descripción" }}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}