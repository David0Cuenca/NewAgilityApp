package com.example.newagilityapp.activites.components.uielements

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.newagilityapp.activites.components.dialogs.NewTaskDialog
import com.example.newagilityapp.model.Task
import com.example.newagilityapp.utilities.Priority
import com.example.newagilityapp.utilities.changeMillisToDateString


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddTasks(
    projectId:Int,
    tasks:List<Task>,
    onAddTask:(Task)->Unit
) {
    var isOpenAddTask by rememberSaveable { mutableStateOf(false) }
    var taskName by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(Priority.MEDIUM) }
    var selectedDateMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }

    NewTaskDialog(
        isOpenAddTask,
        title = "Añadir Tarea",
        taskName = taskName,
        taskDescription = taskDescription,
        selectedPriority = selectedPriority,
        selectedDateMillis = selectedDateMillis,
        onTaskNameChange = { taskName = it },
        onTaskDescriptionChange = { taskDescription = it },
        onPriorityChange = { selectedPriority = it },
        onDateChange = { selectedDateMillis = it },
        onDismissRequest = { isOpenAddTask = false },
        onConfirmButtonsClick = {
            if (taskName.isNotBlank()) {
                onAddTask(
                    Task(
                        title = taskName,
                        description = taskDescription,
                        isDone = false,
                        priority = selectedPriority,
                        taskProjectId = projectId,
                        endate = selectedDateMillis.changeMillisToDateString()
                    )
                )
                isOpenAddTask = false
                taskName = ""
                taskDescription = ""
            }
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
            IconButton(onClick = { isOpenAddTask = true }) {
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
            tasks.forEach { task ->
                TaskCard(task = task)
            }
        }
    }
}

@Composable
private fun TaskCard(task: Task) {
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
