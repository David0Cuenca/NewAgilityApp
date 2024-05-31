package com.example.newagilityapp.activites.project

import android.app.TimePickerDialog
import android.os.Build
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.newagilityapp.activites.components.AddTasks
import com.example.newagilityapp.activites.components.LoadingAnimationDialog
import com.example.newagilityapp.activites.components.TaskDatePicker
import com.example.newagilityapp.data.viewmodels.ProjectViewModel
import com.example.newagilityapp.model.Project
import com.example.newagilityapp.model.Task
import com.example.newagilityapp.utilities.Priority
import com.example.newagilityapp.utilities.changeMillisToDateString
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewProjectScreen(
    navigationController: NavHostController,
    projectId:Int? = null
) {
    val context = LocalContext.current

    val projectViewModel: ProjectViewModel = hiltViewModel()

    val scope = rememberCoroutineScope()
    var isTimePickerOpen by rememberSaveable { mutableStateOf(false) }
    var isDatePickerOpen by rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli()
    )

    var expanded by remember { mutableStateOf(false) }

    var selectedText by remember { mutableStateOf(Priority.MEDIUM) }
    var goalHoursAndMinutes by rememberSaveable { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var taskTitleError by rememberSaveable { mutableStateOf<String?>(null) }


    var isLoading by rememberSaveable { mutableStateOf(false) }
    var isSuccess by rememberSaveable { mutableStateOf<Boolean?>(null) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

/*    LaunchedEffect(projectId) {
        if(isEditing){
            projectId?.let {
                val project = projectViewModel.getProjectById(it).firstOrNull()
                project?.let {
                    title = it.name
                    goalHoursAndMinutes = "${it.goalHours.toInt()}:${((it.goalHours - it.goalHours.toInt()) * 60).toInt()}"
                    description = it.description
                }
            }
        }
    }*/

    taskTitleError = when {
        title.isBlank() -> ""
        title.length < 4 -> "Titulo muy corto."
        title.length > 30 -> "Titulo muy largo."
        else -> null
    }

    if (isTimePickerOpen) {
        val timePickerDialog = TimePickerDialog(
            context,
            //Hay que hacer que se cambie el tema, por el general
            { _, hour: Int, minute: Int ->
                goalHoursAndMinutes = String.format("%02d:%02d", hour, minute)
                isTimePickerOpen = false
            },
            0, 0, true
        )
        timePickerDialog.setOnDismissListener {
            isTimePickerOpen = false
        }
        timePickerDialog.show()
    }

    LoadingAnimationDialog(
        isLoading = isLoading,
        isSuccess = isSuccess,
        errorMessage = errorMessage,
        onDismiss = {
            isLoading = false
            isSuccess = null
            errorMessage = null
        }
    )

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
                .padding(horizontal = 12.dp)
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
            Spacer(modifier = Modifier.height(5.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = description,
                onValueChange = { description = it },
                label = { Text(text = "Descripción") }
            )
            Spacer(modifier = Modifier.height(20.dp))
            //Hay que hacer que solo se puedan elegir horas y minutos, para evitar conflictos
            OutlinedBox(
                modifier = Modifier
                    .size(width = 130.dp, height = 50.dp)
                    .align(Alignment.CenterHorizontally),
                value = goalHoursAndMinutes,
                onClick = { isTimePickerOpen = true },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Selecciona tiempo objetivo"
                    )
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
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
                Spacer(modifier = Modifier.width(16.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        text = "Prioridad",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedText.title,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .clickable { expanded = true }
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            Priority.entries.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item.title) },
                                    onClick = {
                                        selectedText = item
                                        expanded = false
                                        Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }
                    }
                }
            }
            Button(
                enabled = taskTitleError == null,
                onClick = {
                    if (title.isNotBlank() && goalHoursAndMinutes.isNotBlank()) {
                        val hoursMinutesArray = goalHoursAndMinutes.split(":").map { it.toInt() }
                        val goalHours = hoursMinutesArray[0].toFloat() + (hoursMinutesArray[1].toFloat() / 60)
                        scope.launch {
                            isLoading = true
                            try {
                                val newProject = Project(
                                    name = title,
                                    endDate = datePickerState.selectedDateMillis.changeMillisToDateString(),
                                    goalHours = goalHours,
                                    description = description
                                )
                                projectViewModel.insertProject(newProject)
                                isSuccess = true
                                navigationController.popBackStack()
                            } catch (e: Exception) {
                                isSuccess = false
                                errorMessage = e.message
                            } finally {
                                isLoading = false
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