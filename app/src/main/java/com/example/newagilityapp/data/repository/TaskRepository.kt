package com.example.newagilityapp.data.repository

import com.example.newagilityapp.data.local.TaskDao
import com.example.newagilityapp.model.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao) {
    fun getTaskById(taskId: Int): Flow<Task?> = taskDao.getTaskById(taskId)
    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()
    suspend fun addOrUpdateTask(task: Task) = taskDao.upsertTask(task)
    suspend fun deleteTask(taskId: Int) = taskDao.deleteTask(taskId)
}