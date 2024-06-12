package com.example.newagilityapp.activites.components.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.newagilityapp.R
import com.example.newagilityapp.model.Project
import com.example.newagilityapp.model.Task
import com.example.newagilityapp.utilities.formatElapsedTime

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Channel Name"
        val descriptionText = "Channel Description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

fun showDailyReportNotification(context: Context, tasks: List<Task>, projects: List<Project>) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val contentText = buildNotificationContent(tasks, projects)
    val builder = NotificationCompat.Builder(context, "CHANNEL_ID")
        .setSmallIcon(R.drawable.task_icon)
        .setContentTitle("Reporte diario")
        .setContentText(contentText)
        .setStyle(NotificationCompat.BigTextStyle().bigText(contentText))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setOnlyAlertOnce(true)
    notificationManager.notify(2, builder.build())
}

fun buildNotificationContent(tasks: List<Task>, projects: List<Project>): String {
    val taskSummary = if (tasks.isNotEmpty()) {
        "Tareas para hoy:\n" + tasks.joinToString("\n") { it.title }
    } else {
        "No hay tareas para hoy."
    }
    val projectSummary = if (projects.isNotEmpty()) {
        "Proyectos que acaban hoy:\n" + projects.joinToString("\n") { it.name }
    } else {
        "No hay proyectos para hoy."
    }

    return "$taskSummary\n$projectSummary"
}

fun showNotification(context: Context, elapsedTime: Long, projectName: String, initial: Boolean) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val builder = NotificationCompat.Builder(context, "CHANNEL_ID")
        .setSmallIcon(R.drawable.session_icon)
        .setContentTitle("Sesión en curso")
        .setContentText("Proyecto: $projectName - Tiempo: ${formatElapsedTime(elapsedTime)}")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setOnlyAlertOnce(!initial)

    notificationManager.notify(1, builder.build())
}


fun cancelNotification(context: Context) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancel(1)
}