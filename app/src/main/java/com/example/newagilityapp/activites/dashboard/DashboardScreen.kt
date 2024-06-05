package com.example.newagilityapp.activites.dashboard


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.LottieDynamicProperty
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.example.newagilityapp.R
import com.example.newagilityapp.activites.components.CountCard
import com.example.newagilityapp.activites.components.Alert
import com.example.newagilityapp.activites.components.ProjectCard
import com.example.newagilityapp.activites.components.ProjectSessions
import com.example.newagilityapp.activites.components.taskList
import com.example.newagilityapp.data.viewmodels.ProjectViewModel
import com.example.newagilityapp.data.viewmodels.SessionViewModel
import com.example.newagilityapp.data.viewmodels.TaskViewModel
import com.example.newagilityapp.model.Project
import com.example.newagilityapp.model.Screens
import com.example.newagilityapp.utilities.formatDuration
import kotlinx.coroutines.launch


@Composable
fun DashboardScreen(
    navigationController: NavController,
    drawerState: DrawerState,
) {
    val projectViewModel: ProjectViewModel = hiltViewModel()
    val sessionViewModel: SessionViewModel = hiltViewModel()
    val taskViewModel: TaskViewModel = hiltViewModel()

    val scope = rememberCoroutineScope()

    var isOpenDelete by rememberSaveable { mutableStateOf(false) }

    Alert(
        isOpen = isOpenDelete,
        title = "Borrar sesión",
        text = "Vas a borrar una sessión de un proyecto \n " +
                "¿Estas seguro de hacerlo? Recuerda que esto no se puede revertir una vez hecho",
        onDismissRequest = { isOpenDelete = false },
        onConfirmButtonsClick = { isOpenDelete = false }
    )
        Scaffold(
            Modifier.fillMaxSize(),
            topBar = {
                DashboardScreenTopBar {
                    scope.launch { drawerState.open() }
                }
            },
        ) { paddingValues ->
            val allProjects by projectViewModel.getAllProjects.collectAsState(initial = emptyList())
            val allSessions by sessionViewModel.getAllSessions.collectAsState(initial = emptyList())
            val allTask by taskViewModel.getAllTasks.collectAsState(initial = emptyList())
            val completedProjectsCount by projectViewModel.completedProjectsCount.observeAsState(0)
            val totalHoursWorked by projectViewModel.totalHoursWorked.observeAsState(0L)
            Box(modifier = Modifier.fillMaxSize()) {
                val back by rememberLottieComposition(LottieCompositionSpec.Asset("background.json") )
                val dynamicProperties = rememberLottieDynamicProperties(
                    rememberLottieDynamicProperty(
                        property = LottieProperty.COLOR,
                        value = MaterialTheme.colorScheme.primary.toArgb(),
                        keyPath = arrayOf("**")
                    )
                )
                LottieAnimation(
                    composition = back,
                    dynamicProperties = dynamicProperties,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier.fillMaxHeight()
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                ) {
                    item {
                        CountCardSection(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            nproyectos = allProjects.size,
                            fproyectos = completedProjectsCount,
                            hours = totalHoursWorked
                        )
                    }
                    item {
                        ProjectsCardSection(
                            projectList = allProjects,
                            onAddIconClicked = { navigationController.navigate(Screens.NewProjectScreen.route) },
                            onProjectCardClick = { projectId ->
                                navigationController.navigate(
                                    Screens.ProjectScreen.createRoute(
                                        projectId
                                    )
                                )
                            }
                        )
                    }
                    taskList(
                        sectionTitle = "Trabajos por hacer",
                        emptyListText = "No tienes trabajos. \n Pulsa para crear una nueva",
                        tasks = allTask,
                        onEmptyClick = { navigationController.navigate(Screens.TaskScreen.route) },
                        onCheckBoxClick = {},
                        onTaskCardClick = { navigationController.navigate(Screens.TaskScreen.route) }
                    )
                    item {
                        Spacer(modifier = Modifier.size(20.dp))
                    }
                    ProjectSessions(
                        sectionTitle = "Sesiones de los proyectos",
                        emptyListText = "No tienes ninguna sesión de Proyectos.\n !Añade una ahora¡",
                        sessions = allSessions,
                        onDeleteIconClick = { isOpenDelete = true }
                    )
                    item {
                        Button(
                            onClick = { navigationController.navigate(Screens.SessionScreen.route) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 48.dp,
                                    vertical = 20.dp
                                )
                        ) {
                            Text(text = "Iniciar sesión de trabajo")
                        }
                    }
                }
            }
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardScreenTopBar(
    onClickDrawer: () -> Unit
) {
    CenterAlignedTopAppBar(
        modifier = Modifier.background(Color.Transparent),
        navigationIcon = {
            IconButton(onClick = { onClickDrawer() }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu"
                )
            }
        },
        title = {
            Icon(painter = painterResource(id = R.drawable.na_logo), contentDescription = "Logo", Modifier.size(200.dp))
        }
    )
}

@Composable
private fun CountCardSection(
    modifier: Modifier,
    nproyectos: Int,
    fproyectos: Int,
    hours: Long,
) {
    Row(horizontalArrangement = Arrangement.SpaceAround){
        CountCard(
            modifier = Modifier
                .weight(1f),
            title = "Proyectos totales",
            count = "$nproyectos"
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            title = "Terminados",
            count = "$fproyectos"
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            title = "Tiempo de Trabajo",
            count = formatDuration(hours)
        )
    }
}

@Composable
private fun ProjectsCardSection(
    projectList: List<Project>,
    emptyListText: String = "No tienes ningun Proyecto. \n Pulsa el botón + para añadir un nuevo proyecto",
    onAddIconClicked: () -> Unit,
    onProjectCardClick: (Int) -> Unit
    ) {
    Spacer(modifier = Modifier.height(20.dp))
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedCard(Modifier
                .weight(1f),
                shape = (RoundedCornerShape(bottomEndPercent = 0, bottomStartPercent = 0, topStartPercent = 20, topEndPercent = if(projectList.isEmpty()) 20 else 0))
            ) {
                Text(
                    text = "Proyectos",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(12.dp)
                )
            }
            if(projectList.isNotEmpty()) {
                Spacer(modifier = Modifier.width(10.dp))
                OutlinedCard(Modifier
                    .weight(0.10f),
                    shape = (RoundedCornerShape(bottomEndPercent = 20, bottomStartPercent = 0, topStartPercent = 0, topEndPercent = 20))
                ) {
                    IconButton(onClick = onAddIconClicked) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Añadir Proyecto"
                        )
                    }
                }
            }
        }
        
        if (projectList.isEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedCard(Modifier
                .fillMaxWidth(),
                shape = (
                        RoundedCornerShape(
                            bottomEndPercent = 10,
                            bottomStartPercent = 10,
                            topStartPercent = 0,
                            topEndPercent = 0
                            )
                        )
            ) {
                IconButton(
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.CenterHorizontally),
                    onClick = onAddIconClicked
                ) {
                    Icon(
                        modifier = Modifier
                            .size(120.dp)
                            .align(Alignment.CenterHorizontally),
                        imageVector = Icons.Outlined.Add,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = emptyListText
                    )
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    text = emptyListText,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        if(projectList.isNotEmpty())
        OutlinedCard(Modifier
            .fillMaxWidth()
            ,shape = (
                    RoundedCornerShape(
                        bottomEndPercent = 10,
                        bottomStartPercent = 10,
                        topStartPercent = 0,
                        topEndPercent = 0))
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(12.dp)
            ) {
                items(projectList) { project ->
                    ProjectCard(
                        projectname = project.name,
                        onClick = { project.projectId?.let { onProjectCardClick(it) } }
                    )
                }
            }
        }
    }
}
