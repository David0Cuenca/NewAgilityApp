package com.example.newagilityapp.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newagilityapp.data.repository.TaskRepository
import com.example.newagilityapp.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {
    val getAllTasks: Flow<List<Task>> = taskRepository.getAllTasks()
    fun getTaskByProjectId(taskProjectId: Int): Flow<List<Task>> {
        return taskRepository.getTaskByProjectId(taskProjectId)
    }

    fun getTotalTasks(projectTaskId: Int): Flow<Int> {
        return taskRepository.getTotalTasks(projectTaskId)
    }

    fun getCompletedTasks(projectTaskId: Int): Flow<Int> {
        return taskRepository.getCompletedTasks(projectTaskId)
    }
    fun getTaskById(taskId: Int): Flow<Task?> {
        return taskRepository.getTaskById(taskId)
    }

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