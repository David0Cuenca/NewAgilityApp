package com.example.newagilityapp.data.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.newagilityapp.data.repository.ProjectRepository
import com.example.newagilityapp.model.Project
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val projectRepository: ProjectRepository
) : ViewModel() {


    val totalProjectsCount: Flow<Int> = projectRepository.getTotalProjectsCount()
    val totalGoalHours: Flow<Float> = projectRepository.getTotalGoalHours()
    val getAllProjects: Flow<List<Project>> = projectRepository.getAllProjects()
    fun getTotalHoursFromSessions(projectId: Int): Flow<Int> = projectRepository.getTotalHoursFromSessions(projectId)

    val completedProjectsCount = projectRepository.getCompletedProjectsCount().asLiveData()

    val totalHoursWorked = projectRepository.getTotalHoursWorked().asLiveData()

    fun getProjectById(projectId: Int): Flow<Project?> {
        return projectRepository.getProjectById(projectId)
    }

    fun insertProject(project: Project) {
        viewModelScope.launch {
            try {
                projectRepository.insertProject(project)
                Log.d("ProjectViewModel", "Project inserted successfully")
            } catch (e: Exception) {
                Log.e("ProjectViewModel", "Error inserting project: ${e.message}")
            }
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