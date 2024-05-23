package com.example.newagilityapp.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newagilityapp.data.repository.ProjectRepository
import com.example.newagilityapp.model.Project
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProjectViewModel(private val projectRepository: ProjectRepository) : ViewModel() {

    val totalProjectsCount: Flow<Int> = projectRepository.getTotalProjectsCount()
    val totalGoalHours: Flow<Float> = projectRepository.getTotalGoalHours()
    val getAllProjects: Flow<List<Project>> = projectRepository.getAllProjects()

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