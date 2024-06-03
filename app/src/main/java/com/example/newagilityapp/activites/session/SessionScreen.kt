package com.example.newagilityapp.activites.session

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.LottieDynamicProperty
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.example.newagilityapp.activites.components.Alert
import com.example.newagilityapp.activites.components.ProjectSessions
import com.example.newagilityapp.activites.components.SubjectListBottomSheet
import com.example.newagilityapp.data.viewmodels.ProjectViewModel
import com.example.newagilityapp.data.viewmodels.SessionViewModel
import com.example.newagilityapp.model.Project
import com.example.newagilityapp.model.Session
import com.example.newagilityapp.notifications.createNotificationChannel
import com.example.newagilityapp.notifications.showNotification
import kotlinx.coroutines.delay

import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen(navigationController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var isBottomSheetOpen by remember { mutableStateOf(false) }
    var selectedProject by remember { mutableStateOf<Project?>(null) }
    var isDeleteDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isAlertDialogOpen by rememberSaveable { mutableStateOf(false) }

    var isRunning by rememberSaveable { mutableStateOf(false) }
    var elapsedTime by rememberSaveable { mutableLongStateOf(0L) }

    val sessionViewModel: SessionViewModel = hiltViewModel()
    val projectViewModel: ProjectViewModel = hiltViewModel()

    val projects by projectViewModel.getAllProjects.collectAsState(emptyList())
    val sessions by sessionViewModel.getAllSessions.collectAsState(emptyList())
    var sessionToDelete by remember { mutableStateOf<Session?>(null) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            createNotificationChannel(context)
            showNotification(context,elapsedTime,selectedProject?.name?:"",true)
            while (isRunning) {
                delay(1000L)
                elapsedTime += 1
                showNotification(context,elapsedTime,selectedProject?.name?:"",false)
            }
        }
    }

    SubjectListBottomSheet(
        sheetState = sheetState,
        isOpen = isBottomSheetOpen,
        projects = projects,
        onDismissRequest = { isBottomSheetOpen = false },
        onSubjectClicked = { project ->
            selectedProject = project
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) isBottomSheetOpen = false
            }
        }
    )

    Alert(
        isOpen = isDeleteDialogOpen,
        title = "¿Borrar sesión?",
        text = "¿Estas seguro de que quieres borrar esta sesión? " +
                "Esta accion no se puede revertir",
        onDismissRequest = { isDeleteDialogOpen = false },
        onConfirmButtonsClick = {
            sessionToDelete?.let { sessionViewModel.deleteSession(it) }
            isDeleteDialogOpen = false
        }
    )

    Alert(
        isOpen = isAlertDialogOpen,
        title = "No has seleccionado ningun Proyecto",
        text = "Debes seleccionar un proyecto para poder añadir su sesión",
        onDismissRequest = { isAlertDialogOpen = false },
        onConfirmButtonsClick = { isAlertDialogOpen = false }
    )

    Scaffold(
        topBar = {
            SessionScreenTopBar(navigationController)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                TimerSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    elapsedTime = elapsedTime
                )
            }
            item {
                RelatedToProjectSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    relatedTo = selectedProject,
                    selectProjectButtonClick = { isBottomSheetOpen = true }
                )
            }
            item {
                ButtonsSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    isRunning = isRunning,
                    startPauseButtonClick = {
                        if (selectedProject == null) {
                            isAlertDialogOpen = true
                        } else {
                            isRunning = !isRunning
                        }
                    },
                    resetButtonClick = { elapsedTime = 0; isRunning = false },
                    finishButtonClick = {
                        isRunning = false
                        if (selectedProject != null) {
                            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            val formattedDate = Instant.now()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                                .format(formatter)
                            val newSession = selectedProject!!.projectId?.let {
                                Session(
                                    projectSessionId = it,
                                    duration = elapsedTime,
                                    date = formattedDate,
                                    fromProject = selectedProject!!.name
                                )
                            }
                            if (newSession != null) {
                                sessionViewModel.addOrUpdateSession(newSession)
                            }
                        }
                    }
                )
            }
            ProjectSessions(
                sectionTitle = "Historial de sesiones",
                emptyListText = "No tienes ninguna sesión de Proyectos",
                sessions = sessions,
                onDeleteIconClick = {
                    isDeleteDialogOpen = true
                    sessionToDelete = it
                }
            )
        }
    }
}

@Composable
fun TimerSection(modifier: Modifier, elapsedTime: Long) {
    val progress by animateFloatAsState(
        targetValue = if (elapsedTime < 60) {
            elapsedTime / 60f
        } else {
            (elapsedTime % 3600) / 3600f
        },
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing), label = ""
    )

    val formattedTime = String.format(
        Locale.getDefault(),
        "%02d:%02d:%02d",
        elapsedTime / 3600,
        (elapsedTime % 3600) / 60,
        elapsedTime % 60
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.size(250.dp),
            strokeWidth = 10.dp,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = StrokeCap.Round
        )
        Text(
            text = formattedTime,
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp)
        )
    }
}

@Composable
fun RelatedToProjectSection(
    modifier: Modifier,
    selectProjectButtonClick: () -> Unit,
    relatedTo: Project?
) {
    Column(modifier = modifier) {
        Text(
            text = "Relacionado con el proyecto",
            style = MaterialTheme.typography.bodyMedium
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = relatedTo?.name ?: "Seleciona un proyecto",
                style = MaterialTheme.typography.bodyLarge
            )
            IconButton(onClick = selectProjectButtonClick) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Seleccionar Proyecto"
                )
            }
        }
    }
}

@Composable
fun ButtonsSection(
    modifier: Modifier,
    startPauseButtonClick: () -> Unit,
    finishButtonClick: () -> Unit,
    isRunning: Boolean,
    resetButtonClick: () -> Unit
) {
    val pauseResume by rememberLottieComposition(LottieCompositionSpec.Asset("Pause_Resume.json"))
    val save by rememberLottieComposition(LottieCompositionSpec.Asset("save.json"))
    val restart by rememberLottieComposition(LottieCompositionSpec.Asset("restart.json"))

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AnimatedLottieButton(
            composition = restart,
            isRunning = false,
            onClick = resetButtonClick
        )
        AnimatedLottieButton(
            composition = pauseResume,
            isRunning = isRunning,
            onClick = startPauseButtonClick
        )
        AnimatedLottieButton(
            composition = save,
            isRunning = false,
            onClick = finishButtonClick
        )
    }
}

@Composable
fun AnimatedLottieButton(
    composition: LottieComposition?,
    isRunning: Boolean,
    onClick: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(false) }
/*    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = MaterialTheme.colorScheme.primary.toArgb(),
            keyPath = arrayOf("**")
        )
    )*/
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
        isPlaying = isPlaying,
        restartOnPlay = true
    )
    val scope = rememberCoroutineScope()

    OutlinedButton(
        onClick = {
            scope.launch {
                isPlaying = true
                onClick()
            }
        }
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            LottieAnimation(
                modifier = Modifier.size(50.dp),
                composition = composition,
/*                dynamicProperties = dynamicProperties,*/
                progress = {progress},
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center
            )
        }
    }
    LaunchedEffect(progress) {
        if (progress == 1f) {
            isPlaying = false
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionScreenTopBar(
    navigationController: NavHostController
){
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {navigationController.popBackStack()}) {
                Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Ir atrás"
                )
            }
        },
        title = {
            Text(
                text = "Sesión de trabajo",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    )
}