package com.example.newagilityapp.data.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.newagilityapp.data.repository.ProjectRepository
import com.example.newagilityapp.data.repository.TaskRepository
import com.example.newagilityapp.model.Project
import com.example.newagilityapp.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {


    val totalProjectsCount: Flow<Int> = projectRepository.getTotalProjectsCount()
    val totalGoalHours: Flow<Float> = projectRepository.getTotalGoalHours()
    val getAllProjects: Flow<List<Project>> = projectRepository.getAllProjects()
    fun getTotalHoursFromSessions(projectId: Int): Flow<Long> = projectRepository.getTotalHoursFromSessions(projectId)

    val completedProjectsCount = projectRepository.getCompletedProjectsCount().asLiveData()

    val totalHoursWorked:LiveData<Long> = projectRepository.getTotalHoursWorked().asLiveData()

    fun getProjectById(projectId: Int): Flow<Project?> {
        return projectRepository.getProjectById(projectId)
    }

    fun insertProject(project: Project) {
        viewModelScope.launch {
            projectRepository.insertProject(project)
        }
    }

    suspend fun updateProject(project: Project) {
        projectRepository.updateProject(project)
    }

    fun deleteProject(projectId: Int) {
        viewModelScope.launch {
            projectRepository.deleteProject(projectId)
        }
    }
}