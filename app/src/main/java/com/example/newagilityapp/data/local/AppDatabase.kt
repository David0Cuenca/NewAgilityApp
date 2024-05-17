package com.example.newagilityapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newagilityapp.model.Project
import com.example.newagilityapp.model.Session
import com.example.newagilityapp.model.Task

/*
@Database(exportSchema = false, entities = [Project::class, Session::class,Task::class], version = 1)
@TypeConverters(ListConverter::class)
abstract class AppDatabase:RoomDatabase() {
    abstract fun ProjectDao(): ProjectDao
    abstract fun TaskDao(): TaskDao
    abstract fun SessionDao(): SessionDao
}*/
