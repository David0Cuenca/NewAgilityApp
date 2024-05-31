package com.example.newagilityapp.activites.listview

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch

//Todo Lista de todos los proyectos, y bottones de filtrado
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
        topBar = { ListScreenTopBar(
            title = "Lista de proyectos",
            drawerState,
            scrollBehavior = scrollBehavior
        ) },

    ) { paddingValue ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ){
            item {
                ListProjects(
                    projects = projects,
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
    onProjectCardClick: (Int) -> Unit
){
    val taskViewModel: TaskViewModel = hiltViewModel()
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
            val taskCount by taskViewModel.getTaskCountByProjectId(projectList.projectId!!).collectAsState(initial = 0)
            ProjectListCard(
                projectname = projectList.name,
                taskcount = taskCount,
                endDate=projectList.endDate,
                progress = 0.1f,
                onClick = { projectList.projectId?.let { onProjectCardClick(it) } }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun ProjectListCard(
    projectname: String,
    endDate: String,
    progress: Float,
    onClick: () -> Unit,
    taskcount: Int

){
    val percentage = remember(progress){
        (progress*100).toInt().coerceIn(0,100)
    }
    ElevatedCard(
        modifier = Modifier
            .clickable{onClick()}
    ) {
        Row (
            Modifier
                .padding(10.dp)
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
                            text = "$taskcount",
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
    drawerState: DrawerState,
    scrollBehavior: TopAppBarScrollBehavior
){
    val scope = rememberCoroutineScope()
    TopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = {scope.launch{drawerState.open()}}
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu"
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
