package com.example.newagilityapp.activites.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.newagilityapp.R
import com.example.newagilityapp.model.Task

fun LazyListScope.taskList(
    sectionTitle: String,
    tasks: List<Task>,
    emptyListText:String,
    onEmptyClick:()-> Unit,
    onTaskCardClick:(Int?) -> Unit,
    onCheckBoxClick: (Task) -> Unit
){
    item {
        Text(
            text = sectionTitle,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(12.dp)
        )
    }
    if(tasks.isEmpty()){
        item {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onEmptyClick() },
                    horizontalAlignment = Alignment.CenterHorizontally,
                ){
                    Icon(
                        modifier = Modifier
                            .size(120.dp),
                        tint = MaterialTheme.colorScheme.primary,
                        painter = painterResource(R.drawable.task_icon),
                        contentDescription = emptyListText
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = emptyListText,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
        }
    } else {
        items(tasks) { task ->
            TaskCard(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                task = task,
                onCheckBoxClick = { onCheckBoxClick(task) },
                onClick = { onTaskCardClick(task.taskId) }
            )
        }
    }
}

@Composable
private fun TaskCard(
    modifier: Modifier = Modifier,
    task: Task,
    onCheckBoxClick:() -> Unit,
    onClick:() -> Unit
){
    ElevatedCard (
        modifier = modifier.clickable{ onClick() }
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            TaskCheckBox(
                isDone = task.isDone,
                borderColor = task.priority.color,
                onCheckBoxClick = onCheckBoxClick
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = task.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (task.isDone){
                        TextDecoration.LineThrough
                    } else TextDecoration.None
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${task.endate}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}