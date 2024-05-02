package com.example.newagilityapp.Activites.dashboard

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.newagilityapp.Activites.dashboard.components.CountCard

@Composable
fun DashboardScreen(){
    Scaffold(
        topBar = { DashboardScreenTopBar() }
    ) {paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ){
            item {
                CountCardSection(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    nproyectos = 2,
                    fproyectos = 3,
                    hours = 12)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardScreenTopBar(){
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "New Agility",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    )
}

@Composable
private fun CountCardSection(
    modifier: Modifier,
    nproyectos: Int,
    fproyectos: Int,
    hours: Int,
){
    Row {
        CountCard(
            modifier = Modifier.weight(1f),
            title = "Proyectos totales",
            count = "$nproyectos"
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            title = "Terminados",
            count = "$fproyectos"
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            title = "Horas de trabajo",
            count = "$hours"
        )
    }
}