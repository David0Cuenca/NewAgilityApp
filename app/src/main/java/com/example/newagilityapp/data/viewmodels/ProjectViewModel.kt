package com.example.newagilityapp.data.viewmodels

import androidx.lifecycle.ViewModel
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

    fun getProjectById(projectId: Int): Flow<Project?> {
        return projectRepository.getProjectById(projectId)
    }

    fun addOrUpdateProject(project: Project) {
        viewModelScope.launch {
            projectRepository.addOrUpdateProject(project)
        }
    }

    fun deleteProject(projectId: Int) {
        viewModelScope.launch {
            projectRepository.deleteProject(projectId)
        }
    }
}