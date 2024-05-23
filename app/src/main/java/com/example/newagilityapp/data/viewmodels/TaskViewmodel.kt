package com.example.newagilityapp.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newagilityapp.data.repository.TaskRepository
import com.example.newagilityapp.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    val allTasks: Flow<List<Task>> = taskRepository.getAllTasks()

    fun addOrUpdateTask(task: Task) {
        viewModelScope.launch {
            taskRepository.addOrUpdateTask(task)
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            taskRepository.deleteTask(taskId)
        }
    }
}