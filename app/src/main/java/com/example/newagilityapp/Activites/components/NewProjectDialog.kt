package com.example.newagilityapp.Activites.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.newagilityapp.model.Project

@Composable
fun NewProjectDialog(
    isOpen:Boolean,
    title: String,
    selectedColors:List<Color>,
    projectname: String,
    goalHours: String,
    onColorChange: (List<Color>) -> Unit,
    onProjectNameChange:(String) -> Unit,
    onGoalHoursChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmButtonsClick: () -> Unit
){
    var projectNameError by rememberSaveable { mutableStateOf<String?>(value = null) }
    var goalHoursError by rememberSaveable { mutableStateOf<String?>(value = null) }

    projectNameError = when {
        projectname.isBlank() -> "Introduzca un nombre"
        projectname.length < 2 -> "¡Nombre demasiado corto!"
        projectname.length > 20 -> "¡Nombre demasiado largo!"
        else -> null
    }
    goalHoursError = when {
        goalHours.isBlank() -> "Introduzca una hora"
        goalHours.toFloatOrNull() == null -> "¡Introduzca una hora valida!"
        goalHours.toFloat() < 1f -> "Introduzca al menos una hora."
        goalHours.toFloat() > 1000f -> "El máximo de horas son 1000."
        else -> null
    }

    if (isOpen) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(text = title) },
            text = {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Project.CardColors.forEach {colors ->
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .border(
                                        width = 2.dp,
                                        color = if (colors == selectedColors) Color.Black
                                        else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .background(brush = Brush.verticalGradient(colors))
                                    .clickable { onColorChange(colors) }
                            )
                        }
                    }
                    OutlinedTextField(
                        value = projectname,
                        onValueChange = onProjectNameChange,
                        label = { Text(text = "Nombre del Proyecto")},
                        singleLine = true,
                        isError = projectNameError != null && projectname.isNotBlank(),
                        supportingText = { Text(text = projectNameError.orEmpty())}
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = goalHours,
                        onValueChange = onGoalHoursChange,
                        label = { Text(text = "Cantidad de sessiones")},
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = goalHoursError != null && goalHours.isNotBlank(),
                        supportingText = { Text(text = goalHoursError.orEmpty())}
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(text = "Cancel")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirmButtonsClick,
                    enabled = projectNameError == null && goalHoursError == null
                ) {
                    Text(text = "Guardar")
                }
            },
        )
    }
}