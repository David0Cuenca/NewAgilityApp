package com.example.newagilityapp.activites.task


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.newagilityapp.activites.components.Alert
import com.example.newagilityapp.activites.components.SubjectListBottomSheet
import com.example.newagilityapp.activites.components.TaskCheckBox
import com.example.newagilityapp.activites.components.TaskDatePicker
import com.example.newagilityapp.data.viewmodels.ProjectViewModel
import com.example.newagilityapp.data.viewmodels.TaskViewModel
import com.example.newagilityapp.model.Task
import com.example.newagilityapp.ui.theme.Red
import com.example.newagilityapp.utilities.Priority
import com.example.newagilityapp.utilities.changeMillisToDateString

import kotlinx.coroutines.launch
import java.time.Instant


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(navigationController: NavHostController) {
    val projectViewModel: ProjectViewModel = hiltViewModel()
    val taskViewModel: TaskViewModel = hiltViewModel()
    val allProjects by projectViewModel.getAllProjects.collectAsState(initial = emptyList())

    var isDeleteDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDatePickerOpen by rememberSaveable { mutableStateOf(false) }

    var datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli()
    )

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var isBottomSheetOpen by remember { mutableStateOf(false) }

    var selectedProject by remember { mutableIntStateOf(0) }
    val selectedProjectName by projectViewModel.getProjectById(selectedProject).collectAsState(initial = null)
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var taskTitleError by rememberSaveable { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(Priority.MEDIUM) }

    taskTitleError = when {
        title.isBlank() -> "Por favor introduzca un titulo."
        title.length < 4 -> "Titulo muy corto."
        title.length > 30 -> "Titulo muy largo."
        else -> null
    }

    Alert(
        isOpen = isDeleteDialogOpen,
        title = "¿Borrar trabajo?",
        text = "¿Estas seguro de que quieres borrar este trabajo? " +
                "Esta accion no se puede revertir",
        onDismissRequest = { isDeleteDialogOpen = false},
        onConfirmButtonsClick = {isDeleteDialogOpen = false}
    )
    
    TaskDatePicker(
        state = datePickerState,
        isOpen = isDatePickerOpen,
        onDismissRequest = {
            isDatePickerOpen = false },
        onConfirmButtonClick = {
            isDatePickerOpen = false
        }
    )
    SubjectListBottomSheet(
        sheetState = sheetState,
        isOpen = isBottomSheetOpen,
        projects = allProjects,
        onDismissRequest = {isBottomSheetOpen = false},
        onSubjectClicked ={project ->
            selectedProject = project.projectId!!
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if(!sheetState.isVisible) isBottomSheetOpen=false
            }
        }
    )
    Scaffold(
        topBar = {
            TaskScreenTopBar(
                isTaskExist = true,
                isDone = false,
                checkboxBorderColor = Red,
                onBackButtonClick = {navigationController.popBackStack()},
                onDeleteButtonClick = { isDeleteDialogOpen = true },
                onCheckBoxClick = {}
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
                onValueChange = {title = it},
                label = { Text(text = "Titulo")},
                singleLine = true,
                isError = taskTitleError != null && title.isNotBlank(),
                supportingText = { Text(text = taskTitleError.orEmpty())}
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = description,
                onValueChange = {description = it},
                label = { Text(text = "Descripción")}
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Fecha final",
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = datePickerState.selectedDateMillis.changeMillisToDateString(),
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(onClick = { isDatePickerOpen = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Seleciona fecha final"
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Prioridad",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row (modifier = Modifier.fillMaxWidth()){
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
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Relacionado con el proyecto",
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isBottomSheetOpen = true },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                    Text(
                        text = selectedProjectName?.name ?:"Seleciona un projecto",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Selecionar Proyecto"
                    )
            }
            Button(
                enabled = taskTitleError == null,
                onClick = {
                    if (title.isNotBlank() && selectedProject != 0) {
                        val newTask = Task(
                            title = title,
                            description = description,
                            taskProjectId = selectedProject,
                            endate = datePickerState.selectedDateMillis.changeMillisToDateString(),
                            priority = selectedText,
                            isDone = false
                        )
                        scope.launch {
                            try {
                                taskViewModel.addOrUpdateTask(newTask)
                                Log.d("TaskScreen", "Task added successfully")
                                navigationController.popBackStack()
                            } catch (e: Exception) {
                                Log.e("TaskScreen", "Error adding task: ${e.message}")
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
                //Hay que impedir que se cree un trabajo si no hay un proyecto
            ) {
                Text(text = "Guardar")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskScreenTopBar(
    isTaskExist: Boolean,
    isDone:Boolean,
    checkboxBorderColor: Color,
    onBackButtonClick:() -> Unit,
    onDeleteButtonClick:() -> Unit,
    onCheckBoxClick:() -> Unit
    ){
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackButtonClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription ="Ir atras"
                )
            }
        },
        title = { Text(
            text = "Trabajo",
            style = MaterialTheme.typography.headlineMedium
        )},
        actions = {
            if (isTaskExist){
                TaskCheckBox(
                    isDone = isDone,
                    borderColor = checkboxBorderColor,
                    onCheckBoxClick = onCheckBoxClick
                )
                IconButton(onClick = onDeleteButtonClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Borrar trabajo"
                    )
                }
            }
        }
    )
}