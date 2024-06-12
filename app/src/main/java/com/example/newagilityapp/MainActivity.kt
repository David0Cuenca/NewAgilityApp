package com.example.newagilityapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.newagilityapp.activites.calendar.CalendarScreen
import com.example.newagilityapp.activites.components.uielements.DrawerContent
import com.example.newagilityapp.activites.components.uielements.MenuItem
import com.example.newagilityapp.activites.dashboard.DashboardScreen
import com.example.newagilityapp.activites.listview.ListScreen
import com.example.newagilityapp.activites.project.NewProjectScreen
import com.example.newagilityapp.activites.project.ProjectScreen
import com.example.newagilityapp.activites.session.SessionScreen
import com.example.newagilityapp.activites.task.TaskScreen
import com.example.newagilityapp.model.Screens
import com.example.newagilityapp.ui.theme.NewAgilityAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
                        composable(Screens.DashboardScreen.route) { DashboardScreen(navigationController,drawerState) }
                        composable(Screens.ListScreen.route){ ListScreen(navigationController,drawerState,)}
                        composable(Screens.TaskScreen.route){ TaskScreen(navigationController)}

                        composable(
                            Screens.ProjectScreen.route,
                            arguments = listOf(navArgument("projectId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val projectId = backStackEntry.arguments?.getInt("projectId")
                            if (projectId != null) {
                                ProjectScreen(navigationController,projectId)
                            }else{
                                error("No hay un Id del projecto")
                            }
                        }
                        composable(Screens.SessionScreen.route){ SessionScreen(navigationController)}
                        composable(Screens.CalendarScreen.route){ CalendarScreen(navigationController,drawerState)}

                        composable(Screens.NewProjectScreen.route) { NewProjectScreen(navigationController) }
                    }
                }
            }
        }
    }
}
val navBaritems = listOf(
    MenuItem(
        title = "Inicio",
        selected = Icons.Filled.Home,
        badge = false,
        unselected = Icons.Outlined.Home,
        destination = Screens.DashboardScreen.route
    ),
    MenuItem(
        title = "Lista de proyectos",
        selected = Icons.AutoMirrored.Filled.List,
        badge = false,
        unselected = Icons.AutoMirrored.Outlined.List,
        destination = Screens.ListScreen.route
    ),
    MenuItem(
        title = "Calendario",
        selected = Icons.Filled.DateRange,
        badge = false,
        unselected = Icons.Outlined.DateRange,
        destination = Screens.CalendarScreen.route
    ),
)
