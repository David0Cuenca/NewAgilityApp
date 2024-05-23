package com.example.newagilityapp.data.repository

import com.example.newagilityapp.data.local.TaskDao
import com.example.newagilityapp.model.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun addOrUpdateTask(task: Task) {
        taskDao.upsertTask(task)
    }

    suspend fun deleteTask(taskId: Int) {
        taskDao.deleteTas(taskId)
    }
}