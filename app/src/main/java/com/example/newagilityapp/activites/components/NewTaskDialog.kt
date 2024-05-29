package com.example.newagilityapp.activites.components


import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.newagilityapp.activites.project.OutlinedBox
import com.example.newagilityapp.utilities.Priority
import com.example.newagilityapp.utilities.changeMillisToDateString
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskDialog(
    isOpen:Boolean,
    title: String,
    taskName: String,
    taskDescription: String,
    selectedPriority: Priority,
    selectedDateMillis: Long,
    onTaskNameChange: (String) -> Unit,
    onTaskDescriptionChange: (String) -> Unit,
    onPriorityChange: (Priority) -> Unit,
    onDateChange: (Long) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmButtonsClick: () -> Unit
) {
    val context = LocalContext.current

    var taskNameError by rememberSaveable { mutableStateOf<String?>(value = null) }

    var isDatePickerOpen by rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli()
    )

    var expanded by remember { mutableStateOf(false) }

    taskNameError = when {
        taskName.isBlank() -> "Introduzca un nombre"
        taskName.length < 2 -> "¡Nombre demasiado corto!"
        taskName.length > 30 -> "¡Nombre demasiado largo!"
        else -> null
    }

    TaskDatePicker(
        state = datePickerState,
        isOpen = isDatePickerOpen,
        onDismissRequest = { isDatePickerOpen = false },
        onConfirmButtonClick = {
            onDateChange(datePickerState.selectedDateMillis ?: selectedDateMillis)
            isDatePickerOpen = false
        }
    )
        if(isOpen){
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(text = title) },
            text = {
                Column {
                    OutlinedTextField(
                        value = taskName,
                        onValueChange = onTaskNameChange,
                        label = { Text(text = "Nombre de la Tarea") },
                        singleLine = true,
                        isError = taskNameError != null && taskName.isNotBlank(),
                        supportingText = { Text(text = taskNameError.orEmpty()) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = taskDescription,
                        onValueChange = onTaskDescriptionChange,
                        label = { Text(text = "Descripción de la Tarea") },
                        singleLine = false,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                                    value = selectedPriority.title,
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
                                                onPriorityChange(item)
                                                expanded = false
                                                Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show()
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedBox(
                        value = selectedDateMillis.changeMillisToDateString(),
                        onClick = { isDatePickerOpen = true },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Selecciona fecha final"
                            )
                        }
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(text = "Cancelar")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirmButtonsClick,
                    enabled = taskNameError == null
                ) {
                    Text(text = "Guardar")
                }
            },
        )
        }
    }
