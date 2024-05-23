package com.example.newagilityapp.activites.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newagilityapp.model.Project
import com.example.newagilityapp.model.Task

@Preview
@Composable
fun AddTasks() {
    var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }
    var isOpenEditProject by rememberSaveable { mutableStateOf(false) }
    var projectName by remember { mutableStateOf("") }
    var goalHours by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(Project.CardColors.random()) }

    NewProjectDialog(
        isOpen = isOpenEditProject,
        title = "Añadir Proyecto",
        selectedColors = selectedColor,
        projectname = projectName,
        goalHours = goalHours,
        onColorChange = { selectedColor = it },
        onProjectNameChange = { projectName = it },
        onGoalHoursChange = { goalHours = it },
        onDismissRequest = { isOpenEditProject = false },
        onConfirmButtonsClick = {
            tasks = tasks + Task(
                title = projectName,
                endate = System.currentTimeMillis(),
                taskId = tasks.size + 1,
                fromProject = "s",
                isDone = false,
                priority = 1,
                taskProjectId = 1,
                description = ""
            )
            isOpenEditProject = false
        },
    )

    Column(
        Modifier
            .fillMaxSize()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Tareas",
                style = MaterialTheme.typography.headlineMedium
            )
            IconButton(onClick = { isOpenEditProject = true }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Nuevo Trabajo"
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (tasks.isEmpty()) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = "No hay ningun trabajo para este proyecto " +
                    "\n ¡Añade uno ahora!")
                Spacer(modifier = Modifier.height(10.dp))
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = "Crear"
                )
            }
        } else {
            tasks.forEach() { task ->
                TaskCard(task = task)
            }
        }
    }
}

@Composable
fun TaskCard(task: Task) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = task.endate.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = "Editar Trabajo")
            }
        }
    }
}
