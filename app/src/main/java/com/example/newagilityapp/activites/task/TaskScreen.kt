package com.example.newagilityapp.activites.task


import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.newagilityapp.activites.components.DeleteDialog
import com.example.newagilityapp.activites.components.SubjectListBottomSheet
import com.example.newagilityapp.activites.components.TaskCheckBox
import com.example.newagilityapp.activites.components.TaskDatePicker
import com.example.newagilityapp.model.Project
import com.example.newagilityapp.projects
import com.example.newagilityapp.ui.theme.Red
import com.example.newagilityapp.utilities.Priority
import com.example.newagilityapp.utilities.changeMillisToDateString
import kotlinx.coroutines.launch
import java.time.Instant

data class TaskScreenNavArgs(
    val taskId: Int?,
    val projectId: Int?
)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(navigationController: NavHostController) {
    val onBackButtonClick: () -> Unit
    var isDeleteDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDatePickerOpen by rememberSaveable { mutableStateOf(false) }
    var datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli()
    )

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var isBottomSheetOpen by remember { mutableStateOf(false) }

    var selectedProject by remember { mutableStateOf<Project?>(null) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var taskTitleError by rememberSaveable { mutableStateOf<String?>(null) }

    taskTitleError = when {
        title.isBlank() -> "Por favor introduzca un titulo."
        title.length < 4 -> "Titulo muy corto."
        title.length > 30 -> "Titulo muy largo."
        else -> null
    }

    DeleteDialog(
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
        projects = projects,
        onDismissRequest = {isBottomSheetOpen = false},
        onSubjectClicked ={project ->
            selectedProject = project
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
                Priority.entries.forEach{ priority ->
                    PriorityButton(
                        modifier = Modifier.weight(1f),
                        label = priority.title,
                        backgroundColor = priority.color,
                        borderColor = if(priority == Priority.MEDIUM){
                            Color.White
                        }else Color.Transparent,
                        labelColor =  if(priority == Priority.MEDIUM){
                            Color.White
                        }else Color.White.copy(alpha = 0.7f),
                        onClick = {}
                    )
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
            ){
                    Text(
                        text = selectedProject?.name ?:"Seleciona un projecto",
                        style = MaterialTheme.typography.bodyLarge
                    )
                IconButton(onClick = { isBottomSheetOpen = true }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Selecionar Proyecto"
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
    backgroundColor:Color,
    borderColor: Color,
    labelColor:Color,
    onClick:()-> Unit
){
    Box(
        modifier = modifier
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(5.dp)
            .border(1.dp, borderColor, RoundedCornerShape(5.dp))
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ){
        Text(text = label, color = labelColor)
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