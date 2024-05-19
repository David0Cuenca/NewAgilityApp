package com.example.newagilityapp.activites.session

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.newagilityapp.activites.components.DeleteDialog
import com.example.newagilityapp.activites.components.SubjectListBottomSheet
import com.example.newagilityapp.activites.components.projectSessionsList
import com.example.newagilityapp.model.Project
import com.example.newagilityapp.projects
import com.example.newagilityapp.sesions
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen(navigationController: NavHostController) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var isBottomSheetOpen by remember { mutableStateOf(false) }
    var selectedProject by remember { mutableStateOf<Project?>(null) }
    var isDeleteDialogOpen by rememberSaveable { mutableStateOf(false) }


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
    DeleteDialog(
        isOpen = isDeleteDialogOpen,
        title = "¿Borrar sesión?",
        text = "¿Estas seguro de que quieres borrar esta sesión? " +
                "Esta accion no se puede revertir",
        onDismissRequest = { isDeleteDialogOpen = false},
        onConfirmButtonsClick = {isDeleteDialogOpen = false}
    )
    Scaffold (
        topBar={
            SessionScreenTopBar (navigationController)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item{
                TimerSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
            }
            item {
                RelatedToProjectSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    relatedTo = selectedProject,
                    selectProjectButtonClick = { isBottomSheetOpen = true }
                )
            }
            item {
                ButtonsSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    startButtonClick = {  },
                    cancelButtonClick = { },
                    finishButtonClick = {}
                )
            }
            projectSessionsList(
                sectionTitle = "Historial de sesiones",
                emptyListText = "No tienes ninguna sesión de Proyectos.\n !Añade una ahora¡",
                sessions = sesions,
                onDeleteIconClick = { isDeleteDialogOpen = true }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionScreenTopBar(
    navigationController: NavHostController
){
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {navigationController.popBackStack()}) {
                Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Ir atrás"
                )
            }
        },
        title = {
            Text(
                text = "Sesión de trabajo",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    )
}
@Composable
private fun TimerSection(
    modifier: Modifier
){
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
      Box(
          modifier = Modifier
              .size(250.dp)
              .border(5.dp, MaterialTheme.colorScheme.surfaceVariant, CircleShape)
      )
      Text(
          text = "00:00:00" ,
          style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp)
      )
    }
}
@Composable
private fun RelatedToProjectSection(
    modifier: Modifier,
    selectProjectButtonClick: () -> Unit,
    relatedTo: Project?
) {

    Column(modifier = modifier) {
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
                text = relatedTo?.name ?:"Seleciona un projecto",
                style = MaterialTheme.typography.bodyLarge
            )
            //Todo(Hay que comprobar que no se pueda acptar si no se seleciona un proyecto)
            IconButton(onClick =  selectProjectButtonClick ) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Selecionar Proyecto"
                )
            }
        }
    }
}

@Composable
private fun ButtonsSection(
    modifier: Modifier,
    startButtonClick: () -> Unit,
    cancelButtonClick: () -> Unit,
    finishButtonClick:() -> Unit
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = cancelButtonClick) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                text = "Cancelar"
            )
        }
        Button(onClick = startButtonClick) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                text = "Empezar"
            )
        }
        Button(onClick = finishButtonClick) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                text = "Parar"
            )
        }
    }
}
