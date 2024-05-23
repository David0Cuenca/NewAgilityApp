package com.example.newagilityapp.data.repository

import com.example.newagilityapp.data.local.ProjectDao
import com.example.newagilityapp.model.Project
import kotlinx.coroutines.flow.Flow

class ProjectRepository(private val projectDao: ProjectDao) {

    fun getTotalProjectsCount(): Flow<Int> = projectDao.getTotalProjectCount()

    fun getTotalGoalHours(): Flow<Float> = projectDao.getTotalGoalHours()

    fun getAllProjects(): Flow<List<Project>> = projectDao.getAllProjects()

    suspend fun getProjectById(id: Int): Project? = projectDao.getProjectById(id)

    suspend fun addOrUpdateProject(project: Project) {
        projectDao.upsertProject(project)
    }

    suspend fun deleteProject(projectId: Int) {
        projectDao.deleteProject(projectId)
    }
}