package com.example.newagilityapp.Activites.dashboard.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun newProjectDialog(
    onDismissRequest: () -> Unit,
    onConfirmButtonsClick: () -> Unit
){
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "")},
        text = {
            
        },
        confirmButton = {
            TextButton(onClick = onConfirmButtonsClick) {
                Text(text = "Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Cancel")
            }
        }
    )
}