package com.example.newagilityapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.newagilityapp.activites.NavGraphs
import com.example.newagilityapp.model.Project
import com.example.newagilityapp.model.Session
import com.example.newagilityapp.model.Task
import com.example.newagilityapp.ui.theme.NewAgilityAppTheme
import com.ramcosta.composedestinations.DestinationsNavHost
//Todo(Empezar a crear el room #10)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewAgilityAppTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
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
    Task(title = "Hacer cosas", description = "Seguir asi", endate = 0L, taskProjectId = 0, fromProject = "",false, taskId = 0, priority = 0),
    Task(title = "Hacer cosas", description = "Seguir asi", endate = 0L, taskProjectId = 0, fromProject = "",false, taskId = 0, priority = 1),
    Task(title = "Hacer cosas", description = "Seguir asi", endate = 0L, taskProjectId = 0, fromProject = "",true, taskId = 0, priority = 2),
    Task(title = "Hacer cosas", description = "Seguir asi", endate = 0L, taskProjectId = 0, fromProject = "",false, taskId = 0, priority = 3),
    Task(title = "Hacer cosas", description = "Seguir asi", endate = 0L, taskProjectId = 0, fromProject = "",true, taskId = 0, priority = 1),
)

val sesions = listOf(
    Session(fromProject = "Aldi", date = 0L, duration = 2, projectSessionId = 0, sessionId = 0),
    Session(fromProject = "Cien", date = 0L, duration = 2, projectSessionId = 0, sessionId = 0),
    Session(fromProject = "Aldi", date = 0L, duration = 2, projectSessionId = 0, sessionId = 0),
    Session(fromProject = "Agility", date = 0L, duration = 2, projectSessionId = 0, sessionId = 0),
    Session(fromProject = "Aldi", date = 0L, duration = 2, projectSessionId = 0, sessionId = 0),
)