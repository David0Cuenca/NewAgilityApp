package com.example.newagilityapp.data.repository

import android.util.Log
import com.example.newagilityapp.data.local.ProjectDao
import com.example.newagilityapp.model.Project
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProjectRepository @Inject constructor(private val projectDao: ProjectDao) {
    fun getProjectById(projectId: Int): Flow<Project?> = projectDao.getProjectById(projectId)
    fun getTotalHoursFromSessions(projectId: Int): Flow<Int> = projectDao.getTotalHoursFromSessions(projectId)
    fun getTotalProjectsCount(): Flow<Int> = projectDao.getTotalProjectCount()
    fun getCompletedProjectsCount(): Flow<Int> {
        return projectDao.getCompletedProjectsCount()
    }
    fun getTotalHoursWorked(): Flow<Int> {
        return projectDao.getTotalHoursWorked()
    }
    fun getTotalGoalHours(): Flow<Float> = projectDao.getTotalGoalHours()
    fun getAllProjects(): Flow<List<Project>> = projectDao.getAllProjects()
    suspend fun insertProject(project: Project) {
        try {
            projectDao.insertProject(project)
            Log.d("ProjectRepository", "Project inserted successfully")
        } catch (e: Exception) {
            Log.e("ProjectRepository", "Error inserting project: ${e.message}")
        }
    }
    suspend fun updateProject(project: Project) {
        projectDao.updateProject(project)
    }

    suspend fun deleteProject(projectId: Int) = projectDao.deleteProject(projectId)
}