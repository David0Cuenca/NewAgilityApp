package com.example.newagilityapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.newagilityapp.model.Project
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM project WHERE id = :projectId")
    fun getProjectById(projectId: Int): Flow<Project>

    @Query("SELECT COUNT(*) FROM project")
    fun getTotalProjectCount(): Flow<Int>

    @Query("SELECT SUM(goalHours) FROM project")
    fun getTotalGoalHours(): Flow<Float>

    @Query("SELECT * FROM project")
    fun getAllProjects(): Flow<List<Project>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProject(project: Project)

    @Query("DELETE FROM project WHERE id = :projectId")
    suspend fun deleteProject(projectId: Int)
}