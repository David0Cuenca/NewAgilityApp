package com.example.newagilityapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import dagger.hilt.android.HiltAndroidApp



@HiltAndroidApp
class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val sessionDescription = "Cronómetro de la sesion del proyecto"
            val dailyDescription = "Notificación de las tareas y proyectos para el dia de hoy"

            val sesionChannel = NotificationChannel(
                "session_channel",
                "Sesion de Proyecto",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            sesionChannel.description = sessionDescription

            val dailyChannel = NotificationChannel(
                "daily_channel",
                "Reporte diario",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            dailyChannel.description = dailyDescription

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(sesionChannel)
            notificationManager.createNotificationChannel(dailyChannel)
        }
    }
}