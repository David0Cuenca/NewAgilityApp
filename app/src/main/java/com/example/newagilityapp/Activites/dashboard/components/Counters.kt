package com.example.newagilityapp.Activites.dashboard.components

import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CountCard(
    modifier: Modifier = Modifier,
    title: String,
    count: String
){
    ElevatedCard (modifier = modifier){
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = count,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 30.sp)
            )
        }
    }
}