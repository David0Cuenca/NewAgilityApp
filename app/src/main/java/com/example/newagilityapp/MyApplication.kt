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

            val descriptionText = "Cronómetro de la sesion del proyecto"
            val channel = NotificationChannel(
                "session_channel",
                "Sesion de Proyecto",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = descriptionText

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}