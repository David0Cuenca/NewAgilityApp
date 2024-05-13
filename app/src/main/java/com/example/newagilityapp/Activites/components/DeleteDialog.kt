package com.example.newagilityapp.Activites.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DeleteDialog(
    isOpen:Boolean,
    title: String,
    text: String,
    onDismissRequest: () -> Unit,
    onConfirmButtonsClick: () -> Unit
){

    if (isOpen) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(text = title) },
            text = { Text(text = text) },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(text = "Cancelar")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirmButtonsClick,
                ) {
                    Text(text = "Aceptar")
                }
            },
        )
    }
}