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
    @Query("SELECT * FROM project WHERE projectId = :projectId")
    fun getProjectById(projectId: Int): Flow<Project?>

    @Query("SELECT SUM(duration) FROM session WHERE projectSessionId = :projectId")
    fun getTotalHoursFromSessions(projectId: Int): Flow<Int>

    @Query("""
        SELECT COUNT(*) 
        FROM project 
        WHERE projectId IN (
            SELECT taskProjectId 
            FROM task 
            GROUP BY taskProjectId 
            HAVING SUM(CASE WHEN isDone THEN 0 ELSE 1 END) = 0)
    """)
    fun getCompletedProjectsCount(): Flow<Int>

    @Query("SELECT SUM(duration) FROM session")
    fun getTotalHoursWorked(): Flow<Int>

    @Query("SELECT COUNT(*) FROM project")
    fun getTotalProjectCount(): Flow<Int>

    @Query("SELECT SUM(goalHours) FROM project")
    fun getTotalGoalHours(): Flow<Float>

    @Query("SELECT * FROM project")
    fun getAllProjects(): Flow<List<Project>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProject(project: Project)

    @Query("DELETE FROM project WHERE projectId = :projectId")
    suspend fun deleteProject(projectId: Int)
}