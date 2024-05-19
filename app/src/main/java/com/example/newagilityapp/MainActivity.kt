package com.example.newagilityapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newagilityapp.activites.calendar.CalendarScreen
import com.example.newagilityapp.activites.components.DrawerContent
import com.example.newagilityapp.activites.components.MenuItem
import com.example.newagilityapp.activites.dashboard.DashboardScreen
import com.example.newagilityapp.activites.listview.ListScreen
import com.example.newagilityapp.activites.project.ProjectScreen
import com.example.newagilityapp.activites.session.SessionScreen
import com.example.newagilityapp.activites.task.TaskScreen
import com.example.newagilityapp.model.Project
import com.example.newagilityapp.model.Screens
import com.example.newagilityapp.model.Session
import com.example.newagilityapp.model.Task
import com.example.newagilityapp.ui.theme.NewAgilityAppTheme


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewAgilityAppTheme {
                val drawerState= rememberDrawerState(initialValue = DrawerValue.Closed)
                val navigationController = rememberNavController()
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    gesturesEnabled = drawerState.isOpen,
                    drawerContent = {
                        ModalDrawerSheet {
                            DrawerContent(
                                items = navBaritems,
                                modifier = Modifier,
                                navigationController,
                                drawerState
                            )
                        }
                    }
                ) {
                    NavHost(
                        navController = navigationController,
                        startDestination = Screens.DashboardScreen.route
                    ) {
                        composable(Screens.DashboardScreen.route,
                            enterTransition = {
                               slideInHorizontally(
                                    animationSpec = tween(
                                        300, easing = LinearEasing
                                    )
                                ) + slideIntoContainer(
                                    animationSpec = tween(300, easing = EaseIn),
                                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                                )
                            },
                            exitTransition = {
                               slideOutHorizontally(
                                    animationSpec = tween(
                                        300, easing = LinearEasing
                                    )
                                ) + slideOutOfContainer(
                                    animationSpec = tween(300, easing = EaseOut),
                                    towards = AnimatedContentTransitionScope.SlideDirection.End
                                )
                            }
                        ) { DashboardScreen(navigationController,drawerState) }
                        composable(Screens.ListScreen.route,
                            enterTransition = {
                               slideInHorizontally(
                                    animationSpec = tween(
                                        300, easing = LinearEasing
                                    )
                                ) + slideIntoContainer(
                                    animationSpec = tween(300, easing = EaseIn),
                                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                                )
                            },
                            exitTransition = {
                               slideOutHorizontally(
                                    animationSpec = tween(
                                        300, easing = LinearEasing
                                    )
                                ) + slideOutOfContainer(
                                    animationSpec = tween(300, easing = EaseOut),
                                    towards = AnimatedContentTransitionScope.SlideDirection.End
                                )
                            }
                        ){ ListScreen(navigationController,drawerState)}
                        composable(Screens.TaskScreen.route,
                            enterTransition = {
                               slideInHorizontally(
                                    animationSpec = tween(
                                        300, easing = LinearEasing
                                    )
                                ) + slideIntoContainer(
                                    animationSpec = tween(300, easing = EaseIn),
                                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                                )
                            },
                            exitTransition = {
                               slideOutHorizontally(
                                    animationSpec = tween(
                                        300, easing = LinearEasing
                                    )
                                ) + slideOutOfContainer(
                                    animationSpec = tween(300, easing = EaseOut),
                                    towards = AnimatedContentTransitionScope.SlideDirection.End
                                )
                            }){ TaskScreen(navigationController)}
                        composable(Screens.ProjectScreen.route,
                            enterTransition = {
                               slideInHorizontally(
                                    animationSpec = tween(
                                        300, easing = LinearEasing
                                    )
                                ) + slideIntoContainer(
                                    animationSpec = tween(300, easing = EaseIn),
                                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                                )
                            },
                            exitTransition = {
                               slideOutHorizontally(
                                    animationSpec = tween(
                                        300, easing = LinearEasing
                                    )
                                ) + slideOutOfContainer(
                                    animationSpec = tween(300, easing = EaseOut),
                                    towards = AnimatedContentTransitionScope.SlideDirection.End
                                )
                            }){ ProjectScreen(navigationController)}
                        composable(Screens.SessionScreen.route,
                            enterTransition = {
                               slideInHorizontally(
                                    animationSpec = tween(
                                        300, easing = LinearEasing
                                    )
                                ) + slideIntoContainer(
                                    animationSpec = tween(300, easing = EaseIn),
                                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                                )
                            },
                            exitTransition = {
                               slideOutHorizontally(
                                    animationSpec = tween(
                                        300, easing = LinearEasing
                                    )
                                ) + slideOutOfContainer(
                                    animationSpec = tween(300, easing = EaseOut),
                                    towards = AnimatedContentTransitionScope.SlideDirection.End
                                )
                            }){ SessionScreen(navigationController)}
                        composable(Screens.CalendarScreen.route,
                            enterTransition = {
                               slideInHorizontally(
                                    animationSpec = tween(
                                        300, easing = LinearEasing
                                    )
                                ) + slideIntoContainer(
                                    animationSpec = tween(300, easing = EaseIn),
                                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                                )
                            },
                            exitTransition = {
                               slideOutHorizontally(
                                    animationSpec = tween(
                                        300, easing = LinearEasing
                                    )
                                ) + slideOutOfContainer(
                                    animationSpec = tween(300, easing = EaseOut),
                                    towards = AnimatedContentTransitionScope.SlideDirection.End
                                )
                            }){ CalendarScreen(navigationController,drawerState)}
                    }
                }
            }
        }
    }
}


val projects = listOf(
    Project(name = "Aldi", endDate = "12/12/2020", colors = Project.CardColors[1],1f, projectId = 0),
    Project(name = "Consejos", endDate = "12/12/2020", colors = Project.CardColors[1],2f, projectId = 0),
    Project(name = "Clases", endDate = "12/12/2020", colors = Project.CardColors[1],3f, projectId = 0),
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
val navBaritems = listOf(
    MenuItem(
        title = "Inicio",
        selected = Icons.Filled.Home,
        badge = false,
        unselected= Icons.Outlined.Home,
        destination = Screens.DashboardScreen.route
    ),
    MenuItem(
        title = "Lista de proyectos",
        selected = Icons.Filled.List,
        badge = true,
        unselected = Icons.Outlined.List,
        destination = Screens.ListScreen.route
    ),
    MenuItem(
        title = "Calendario",
        selected = Icons.Filled.DateRange,
        badge = false,
        unselected= Icons.Outlined.DateRange,
        destination = Screens.CalendarScreen.route
),
)
