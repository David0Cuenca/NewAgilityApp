package com.example.newagilityapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.newagilityapp.Activites.dashboard.DashboardScreen
import com.example.newagilityapp.Activites.project.ProjectScreen
import com.example.newagilityapp.Activites.session.SessionScreen
import com.example.newagilityapp.Activites.task.TaskScreen
import com.example.newagilityapp.model.Project
import com.example.newagilityapp.model.Session
import com.example.newagilityapp.model.Task
import com.example.newagilityapp.ui.theme.NewAgilityAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewAgilityAppTheme {
                SessionScreen()
            }
        }
    }
}
val projects = listOf(
    Project(name = "Aldi", EndDate = "12/12/2020", colors = Project.CardColors[1], projectId = 0),
    Project(name = "Consejos", EndDate = "12/12/2020", colors = Project.CardColors[1], projectId = 0),
    Project(name = "Clases", EndDate = "12/12/2020", colors = Project.CardColors[1], projectId = 0),
)

val tasks = listOf(
    Task(title = "Hacer cosas", description = "Seguir asi", endate = 0L, taskProjectId = 0, fromProject = "",false, TaskId = 0, priority = 0),
    Task(title = "Hacer cosas", description = "Seguir asi", endate = 0L, taskProjectId = 0, fromProject = "",false, TaskId = 0, priority = 1),
    Task(title = "Hacer cosas", description = "Seguir asi", endate = 0L, taskProjectId = 0, fromProject = "",true, TaskId = 0, priority = 2),
    Task(title = "Hacer cosas", description = "Seguir asi", endate = 0L, taskProjectId = 0, fromProject = "",false, TaskId = 0, priority = 3),
    Task(title = "Hacer cosas", description = "Seguir asi", endate = 0L, taskProjectId = 0, fromProject = "",true, TaskId = 0, priority = 1),
)

val sesions = listOf(
    Session(fromProject = "Aldi", date = 0L, duration = 2, projectSessionId = 0, sessionId = 0),
    Session(fromProject = "Cien", date = 0L, duration = 2, projectSessionId = 0, sessionId = 0),
    Session(fromProject = "Aldi", date = 0L, duration = 2, projectSessionId = 0, sessionId = 0),
    Session(fromProject = "Agility", date = 0L, duration = 2, projectSessionId = 0, sessionId = 0),
    Session(fromProject = "Aldi", date = 0L, duration = 2, projectSessionId = 0, sessionId = 0),
)