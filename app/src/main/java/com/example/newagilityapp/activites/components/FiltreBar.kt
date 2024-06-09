package com.example.newagilityapp.activites.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FilterBar(
    selectedFilter: FilterType,
    onFilterSelected: (FilterType) -> Unit
) {
    val filterOptions = listOf(FilterType.Tasks, FilterType.Projects, FilterType.Both)

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        filterOptions.forEach { filter ->


            OutlinedButton(
                onClick = { onFilterSelected(filter) },
                border = BorderStroke(2.dp, if(filter==selectedFilter) MaterialTheme.colorScheme.primary else Color.Transparent),
                colors = ButtonDefaults
                    .outlinedButtonColors(
                        contentColor = if(filter==selectedFilter) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inversePrimary
                    ),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            ) {
                Text(text = filter.type)
            }
        }
    }
}
enum class FilterType(val type: String) {
    Tasks("Tareas"),
    Projects("Proyectos"),
    Both("Ambos")
}