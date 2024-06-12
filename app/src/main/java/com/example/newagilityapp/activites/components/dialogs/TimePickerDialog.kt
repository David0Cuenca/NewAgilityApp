package com.example.newagilityapp.activites.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ozcanalasalvar.wheelview.SelectorOptions
import com.ozcanalasalvar.wheelview.WheelView

@Composable
fun TimePickerDialog(
    initialHours: Int = 0,
    initialMinutes: Int = 0,
    onTimeSelected: (hours: Int, minutes: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedHours by remember { mutableStateOf(initialHours) }
    var selectedMinutes by remember { mutableStateOf(initialMinutes) }

    Dialog(onDismissRequest = onDismiss) {
        OutlinedCard {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Seleccione Horas y Minutos", style = MaterialTheme.typography.headlineSmall)

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WheelView(
                        modifier = Modifier.size(100.dp, 120.dp),
                        itemSize = DpSize(60.dp, 40.dp),
                        selection = selectedHours,
                        itemCount = 100,
                        selectorOption = SelectorOptions(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.onBackground),
                        rowOffset = 1,
                        onFocusItem = { index ->
                            selectedHours = index
                        },
                        content = {
                            Text(text = it.toString())
                        }
                    )

                    Spacer(modifier = Modifier.width(24.dp))

                    WheelView(
                        modifier = Modifier.size(100.dp, 120.dp),
                        selectorOption = SelectorOptions(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.onBackground),
                        itemSize = DpSize(60.dp, 40.dp),
                        selection = selectedMinutes,
                        itemCount = 60,
                        rowOffset = 1,
                        onFocusItem = { index ->
                            selectedMinutes = index
                        },
                        content = {
                            Text(text = it.toString())
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    onTimeSelected(selectedHours, selectedMinutes)
                    onDismiss()
                }) {
                    Text(text = "Aceptar")
                }
            }
        }
    }
}
