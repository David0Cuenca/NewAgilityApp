package com.example.newagilityapp.activites.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newagilityapp.data.viewmodels.ProjectViewModel
import com.example.newagilityapp.data.viewmodels.TaskViewModel
import com.example.newagilityapp.model.Project
import com.example.newagilityapp.model.Task
import com.example.newagilityapp.utilities.Priority
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun WeeklySummaryScreen() {
    val projectViewModel: ProjectViewModel = hiltViewModel()
    val taskViewModel: TaskViewModel = hiltViewModel()

    val allTasks by taskViewModel.getAllTasks.collectAsState(initial = emptyList())
    val allProjects by projectViewModel.getAllProjects.collectAsState(initial = emptyList())

    val summary = remember(allTasks, allProjects) {
        categorizeAndPrioritizeTasks(allTasks, allProjects)
    }
    Text(
        modifier = Modifier.padding(10.dp),
        text = "Reporte Semanal",
        style = MaterialTheme.typography.titleLarge
    )
    if (allTasks.isEmpty() && allProjects.isEmpty()) {
        EmptyStateScreen()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Resumen Semanal", style = MaterialTheme.typography.titleMedium)
            summary.forEach { (priority, items) ->
                Text(text = priority, style = MaterialTheme.typography.titleMedium)
                items.forEach { item ->
                    when (item) {
                        is Task -> TaskItem(task = item)
                        is Project -> ProjectItem(project = item)
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyStateScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
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
fun TaskItem(task: Task) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = task.title, style = MaterialTheme.typography.titleSmall)
            Text(text = "Fecha límite: ${task.endate}", style = MaterialTheme.typography.bodyMedium)
            Text(text = task.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun ProjectItem(project: Project) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = project.name, style = MaterialTheme.typography.titleSmall)
            Text(text = "Fecha límite: ${project.endDate}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}


fun categorizeAndPrioritizeTasks(tasks: List<Task>, projects: List<Project>): Map<String, List<Any>> {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

    val categorizedTasks = tasks.filter {
        try {
            val taskDate = LocalDate.parse(it.endate, formatter)
            taskDate.isBefore(LocalDate.now().plusWeeks(1)) || taskDate.isEqual(LocalDate.now().plusWeeks(1))
        } catch (e: Exception) {
            false
        }
    }.groupBy { it.priority }

    val categorizedProjects = projects.filter {
        try {
            val projectDate = LocalDate.parse(it.endDate, formatter)
            projectDate.isBefore(LocalDate.now().plusWeeks(1)) || projectDate.isEqual(LocalDate.now().plusWeeks(1))
        } catch (e: Exception) {
            false
        }
    }.groupBy { projectPriority(it.endDate, formatter) }

    return mapOf(
        "Proyectos Cercanos" to (categorizedProjects["Proyectos Cercanos"] ?: emptyList()),
        "Alta Prioridad" to (categorizedTasks[Priority.HIGH] ?: emptyList()),
        "Media Prioridad" to (categorizedTasks[Priority.MEDIUM] ?: emptyList()),
        "Baja Prioridad" to (categorizedTasks[Priority.LOW] ?: emptyList())
    )
}

private fun projectPriority(endDate: String, formatter: DateTimeFormatter): String {
    return try {
        val date = LocalDate.parse(endDate, formatter)
        if (date.isBefore(LocalDate.now().plusWeeks(1)) || date.isEqual(LocalDate.now().plusWeeks(1))) {
            "Proyectos Cercanos"
        } else {
            "No Prioritario"
        }
    } catch (e: Exception) {
        "No Prioritario"
    }
}
