package com.example.newagilityapp.Activites.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.newagilityapp.Activites.dashboard.components.TaskCheckBox
import com.example.newagilityapp.ui.theme.Red

@Composable
fun TaskScreen(){
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TaskScreenTopBar(
                isTaskExist = true,
                isDone = false,
                checkboxBorderColor = Red,
                onBackButtonClick = {  },
                onDeleteButtonClick = {  },
                onCheckBoxClick = {}
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 12.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = {title = it},
                label = { Text(text = "Titulo")}
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = description,
                onValueChange = {description = it},
                label = { Text(text = "Descripción")}
            )
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