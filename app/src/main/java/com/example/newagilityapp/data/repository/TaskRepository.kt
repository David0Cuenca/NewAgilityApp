package com.example.newagilityapp.data.repository

import com.example.newagilityapp.data.local.TaskDao
import com.example.newagilityapp.model.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao) {
    fun getTaskById(taskId: Int): Flow<Task?> = taskDao.getTaskById(taskId)
    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()
    fun getTaskByProjectId(taskProjectId: Int): Flow<List<Task>> = taskDao.getTaskByProjectId(taskProjectId)
    fun getTotalTasks(projectProjectId: Int): Flow<Int> = taskDao.getTotalTasks(projectProjectId)
    fun getCompletedTasks(projectProjectId: Int): Flow<Int> = taskDao.getCompletedTasks(projectProjectId)
    suspend fun addOrUpdateTask(task: Task) = taskDao.upsertTask(task)
    suspend fun deleteTask(taskId: Int) = taskDao.deleteTask(taskId)
    fun getTaskCountByProjectId(projectId: Int): Flow<Int> {
        return taskDao.getTaskCountByProjectId(projectId)
    }
}