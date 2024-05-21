package com.example.newagilityapp.activites.project

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.newagilityapp.activites.components.DeleteDialog
import com.example.newagilityapp.activites.components.SubjectListBottomSheet
import com.example.newagilityapp.activites.components.TaskCheckBox
import com.example.newagilityapp.activites.components.TaskDatePicker
import com.example.newagilityapp.model.Project
import com.example.newagilityapp.projects
import com.example.newagilityapp.ui.theme.Red
import com.example.newagilityapp.ui.theme.backgroundDark
import com.example.newagilityapp.utilities.Priority
import com.example.newagilityapp.utilities.changeMillisToDateString
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewProjectScreen(navigationController: NavHostController) {
    val context = LocalContext.current

    var isDatePickerOpen by rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli()
    )
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var isBottomSheetOpen by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    var selectedProject by remember { mutableStateOf<Project?>(null) }
    var selectedText by remember { mutableStateOf(Priority.LOW) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var taskTitleError by rememberSaveable { mutableStateOf<String?>(null) }

    taskTitleError = when {
        title.isBlank() -> ""
        title.length < 4 -> "Titulo muy corto."
        title.length > 30 -> "Titulo muy largo."
        else -> null
    }

    // DatePickerDialog
    TaskDatePicker(
        state = datePickerState,
        isOpen = isDatePickerOpen,
        onDismissRequest = { isDatePickerOpen = false },
        onConfirmButtonClick = { isDatePickerOpen = false }
    )

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

    Scaffold(
        topBar = {
            NewProjectScreenTopBar(
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

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = "Fecha final",
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

            Spacer(modifier = Modifier.height(30.dp))
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
                    text = selectedProject?.name ?: "Selecciona un proyecto",
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(onClick = { isBottomSheetOpen = true }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Seleccionar Proyecto"
                    )
                }
            }
            Button(
                enabled = taskTitleError == null,
                onClick = { /*TODO*/ },
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
private fun PriorityButton(
    modifier: Modifier = Modifier,
    label:String,
    backgroundColor: Color,
    labelColor: Color,
    onClick:()-> Unit
){
    Box(
        modifier = modifier
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ){
        Text(text = label, color = labelColor)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewProjectScreenTopBar(
    onBackButtonClick:() -> Unit,
){
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackButtonClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription ="Ir atras"
                )
            }
        },
        title = {
            Text(
                text = "Nuevo Proyecto",
                style = MaterialTheme.typography.headlineMedium
            )
        },

    )
}
