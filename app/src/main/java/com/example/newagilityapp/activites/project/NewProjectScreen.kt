package com.example.newagilityapp.activites.project

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.newagilityapp.R
import com.example.newagilityapp.activites.components.LoadingAnimationDialog
import com.example.newagilityapp.activites.components.TaskDatePicker
import com.example.newagilityapp.activites.components.TimePickerDialog
import com.example.newagilityapp.data.viewmodels.ProjectViewModel
import com.example.newagilityapp.model.Project
import com.example.newagilityapp.utilities.changeMillisToDateString
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewProjectScreen(
    navigationController: NavHostController,
) {
    val projectViewModel: ProjectViewModel = hiltViewModel()

    val scope = rememberCoroutineScope()
    var isTimePickerOpen by rememberSaveable { mutableStateOf(false) }
    var isDatePickerOpen by rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli()
    )

    var goalHoursAndMinutes by rememberSaveable { mutableStateOf("1:00") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var taskTitleError by rememberSaveable { mutableStateOf<String?>(null) }

    var showLoadingDialog by remember { mutableStateOf(false) }

    taskTitleError = when {
        title.isBlank() -> ""
        title.length < 4 -> "Titulo muy corto."
        title.length > 30 -> "Titulo muy largo."
        else -> null
    }

    if (isTimePickerOpen) {
        TimePickerDialog(
            initialHours = goalHoursAndMinutes.split(":")[0].toInt(),
            initialMinutes = goalHoursAndMinutes.split(":")[1].toInt(),
            onTimeSelected = { hours, minutes ->
                goalHoursAndMinutes = String.format(Locale.getDefault(),"%02d:%02d", hours, minutes)
                isTimePickerOpen = false
            },
            onDismiss = {
                isTimePickerOpen = false
            }
        )
    }

    if (showLoadingDialog) {
        LoadingAnimationDialog(
            onDismiss = {
                showLoadingDialog = false
                navigationController.popBackStack()
            }
        )
    }

    TaskDatePicker(
        state = datePickerState,
        isOpen = isDatePickerOpen,
        onDismissRequest = { isDatePickerOpen = false },
        onConfirmButtonClick = { isDatePickerOpen = false }
    )

    Scaffold(
        topBar = {
            NewProjectScreenTopBar(
                title = "Nuevo Proyecto",
                onBackButtonClick = { navigationController.popBackStack() },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(state = rememberScrollState())
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 10.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = { title = it },
                label = { Text(text = "Titulo") },
                singleLine = true,
                isError = taskTitleError != null && title.isNotBlank(),
                supportingText = { Text(text = taskTitleError.orEmpty()) }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = description,
                onValueChange = { description = it },
                label = { Text(text = "Descripción") }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier
                    .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Fecha entrega",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedBox(
                        value = datePickerState.selectedDateMillis.changeMillisToDateString(),
                        onClick = { isDatePickerOpen = true },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Selecciona fecha final"
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))
                Column(Modifier
                    .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Objetivo de horas",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedBox(
                        modifier = Modifier.fillMaxWidth(),
                        value = goalHoursAndMinutes,
                        onClick = { isTimePickerOpen = true },
                        trailingIcon = {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(R.drawable.session_icon),
                                contentDescription = "Selecciona tiempo objetivo"
                            )
                        }
                    )
                }
            }
            Button(
                enabled = taskTitleError == null,
                onClick = {
                    if (title.isNotBlank() && goalHoursAndMinutes.isNotBlank()) {
                        val hoursMinutesArray = goalHoursAndMinutes.split(":").map { it.toInt() }
                        val goalHours = hoursMinutesArray[0].toFloat() + (hoursMinutesArray[1].toFloat() / 60)

                        val newProject = Project(
                            name = title,
                            endDate = datePickerState.selectedDateMillis.changeMillisToDateString(),
                            goalHours = goalHours,
                            description = description
                        )
                        scope.launch {
                            showLoadingDialog = true
                            try {
                                delay(1000)
                                projectViewModel.insertProject(newProject)
                            } catch (e: Exception) {
                                Log.e("NewProjectScreen", "Error during project insertion: ${e.message}")
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            ) {
                Text(text = "Guardar")
            }
        }
    }
}

@Composable
fun OutlinedBox(
    modifier: Modifier = Modifier,
    value: String,
    onClick: () -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null,
    isEnabled: Boolean = true
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = isEnabled) { onClick() }
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)),
                shape = MaterialTheme.shapes.extraSmall
            )
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isEnabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            if (trailingIcon != null) {
                trailingIcon()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewProjectScreenTopBar(
    title:String,
    onBackButtonClick:() -> Unit,
){
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackButtonClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription ="Ir atras"
                )
            }
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium
            )
        },
    )
}