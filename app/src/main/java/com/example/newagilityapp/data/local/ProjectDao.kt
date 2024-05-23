package com.example.newagilityapp.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.newagilityapp.model.Project
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Upsert
    suspend fun upsertProject(project: Project)
    @Query("SELECT COUNT(*) FROM project")
    fun getTotalProjectCount(): Flow<Int>
    @Query("SELECT SUM(goalHours) FROM project")
    fun getTotalGoalHours(): Flow<Float>
    @Query("SELECT * FROM project WHERE projectId = :projectId")
    suspend fun getProjectById(projectId:Int): Project?
    @Query("DELETE FROM project WHERE projectId = :projectId")
    suspend fun deleteProject(projectId: Int)
    @Query("SELECT * FROM project")
    fun getAllProjects(): Flow<List<Project>>

}