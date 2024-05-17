package com.example.newagilityapp.activites.listview

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.Navigation
import com.example.newagilityapp.R
import com.example.newagilityapp.activites.components.BottomNavBar
import com.example.newagilityapp.activites.components.CountCard
import com.example.newagilityapp.activites.components.ProjectCard
import com.example.newagilityapp.activites.destinations.ListScreenRouteDestination
import com.example.newagilityapp.activites.destinations.ProjectScreenRouteDestination
import com.example.newagilityapp.activites.destinations.TaskScreenRouteDestination
import com.example.newagilityapp.activites.project.ProjectScreenNavArgs
import com.example.newagilityapp.activites.task.TaskScreenNavArgs
import com.example.newagilityapp.projects
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

data class ProjectScreenNavArgs(
    val projectId: Int
)

@Destination
@Composable
fun ListScreenRoute(
    navigator: DestinationsNavigator
){
    ListScreen(
        onBackClick = {navigator.navigateUp()},
        onProjectClick ={ projectId ->
            projectId?.let {
                val navArg = ProjectScreenNavArgs(projectId = projectId)
                navigator.navigate(ProjectScreenRouteDestination(navArgs = navArg))
            } },
        onAddTaskClick = {
            val navArg = TaskScreenNavArgs(taskId = null,projectId = -1)
            navigator.navigate(TaskScreenRouteDestination(navArgs = navArg))
        },
        onListClick = {
            navigator.navigate(ListScreenRouteDestination())
        }
    )
}
//Todo Lista de todos los proyectos, y bottones de filtrado
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    onBackClick: () -> Unit,
    onProjectClick: (Int?) -> Unit,
    onAddTaskClick: () -> Unit,
    onListClick: () -> Unit
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val listState = rememberLazyListState()
    val isFABExpanded by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { ListScreenTopBar(
            title = "Lista de proyectos",
            onBackClick = onBackClick,
            scrollBehavior = scrollBehavior
        )
        },
        bottomBar = { },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = "Añadir Trabajo") },
                icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir") },
                onClick = onAddTaskClick,
                expanded = isFABExpanded
            )
        }
    ) { paddingValue ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ){
            item {
                ListProjects(
                    emptyListText = "No hay ningun proyecto",
                    onProjectCardClick = onProjectClick )
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

        }
    }
}
@Composable
private fun ListProjects(
    emptyListText: String,
    onProjectCardClick: (Int?) -> Unit
){
    Column(modifier = Modifier) {

        if(projects.isEmpty()){
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(R.drawable.project_icon),
                contentDescription = emptyListText
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = emptyListText,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )

        }
        projects.forEach { projectList ->
            ProjectListCard(
                projectname = projectList.name,
                endDate=projectList.endDate,
                progress = 0.1f,
                //Trabajos asociados totales
                onClick = { onProjectCardClick(projectList.projectId) }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
@Composable
private fun ProjectListCard(
    projectname: String,
    endDate: String,
    progress:Float,
    onClick: () -> Unit
    //tasks: Int
){
    val percentage = remember(progress){
        (progress*100).toInt().coerceIn(0,100)
    }
    ElevatedCard(
        modifier = Modifier
            .clickable{onClick()}
    ) {
        Row (Modifier.padding(10.dp)
                .fillMaxWidth(),
            ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally){
                Text(
                    text = projectname,
                    style = MaterialTheme.typography.headlineLarge)
                Row (
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                Text(
                    text = "Fecha fin:",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier =Modifier.height(10.dp))
                Text(
                    text = endDate,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
               Text(
                   text = "Trabajos:",
                   style = MaterialTheme.typography.bodyLarge
               )
               Spacer(modifier = Modifier.height(10.dp))
               Text(
                   text = "12",
                   style = MaterialTheme.typography.bodyMedium
               )
           }
                    Box (modifier = Modifier.size(75.dp),
                        contentAlignment = Alignment.Center
                    ){
                        CircularProgressIndicator(
                            modifier = Modifier.fillMaxSize(),
                            progress = 1f,
                            strokeWidth = 4.dp,
                            strokeCap = StrokeCap.Round,
                            color = MaterialTheme.colorScheme.surfaceVariant
                        )
                        CircularProgressIndicator(
                            modifier = Modifier.fillMaxSize(),
                            progress = progress,
                            strokeWidth = 4.dp,
                            strokeCap = StrokeCap.Round
                        )
                        Text(text = "$percentage%")
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListScreenTopBar(
    title: String,
    onBackClick:() -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
){
    LargeTopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Ir atras"
                )
            }
        },
        title = { Text(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineLarge)
        },
    )
}

@Composable
private fun ProjectOverviewSection(
    modifier: Modifier,
    hoursDone: String,
    goalHours:String,
    progress: Float
){
    val percentage = remember(progress){
        (progress*100).toInt().coerceIn(0,100)
    }

    Row (
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ){
        CountCard(
            modifier = Modifier.weight(1f),
            title = "Objetivo de Horas",
            count = goalHours
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            title = "Horas Hechas",
            count = hoursDone
        )
        Spacer(modifier = Modifier.width(10.dp))
        Box (
            modifier = Modifier.size(75.dp),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                progress = 1f,
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                progress = progress,
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round
            )
            Text(text = "$percentage%")
        }
    }
}