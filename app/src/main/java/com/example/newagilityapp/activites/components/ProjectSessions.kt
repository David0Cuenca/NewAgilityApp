package com.example.newagilityapp.activites.components

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.newagilityapp.R
import com.example.newagilityapp.model.Session
import com.example.newagilityapp.utilities.formatDuration


fun LazyListScope.ProjectSessions(
    sectionTitle: String,
    sessions: List<Session>,
    emptyListText: String,
    onDeleteIconClick: (Session) -> Unit
) {
    item {
        Text(
            text = sectionTitle,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(12.dp)
        )
    }
    if (sessions.isEmpty()) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .size(120.dp),
                    tint = MaterialTheme.colorScheme.primary,
                    painter = painterResource(R.drawable.session_icon),
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
    }
    items(sessions) { session ->
        SessionCard(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            session = session,
            onDeleteIconClick = { onDeleteIconClick(session) }
        )
    }
}

@Composable
private fun SessionCard(
    modifier: Modifier,
    session: Session,
    onDeleteIconClick: () -> Unit
) {
    val formattedDuration = formatDuration(session.duration)

    Card(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = session.fromProject,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = session.date,
                    style = MaterialTheme.typography.bodyMedium
                )

            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = formattedDuration,
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(onClick = onDeleteIconClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Borrar sesión"
                )
            }
        }
    }
}


