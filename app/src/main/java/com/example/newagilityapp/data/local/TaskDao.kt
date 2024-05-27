package com.example.newagilityapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.newagilityapp.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM task WHERE taskId = :taskId")
    fun getTaskById(taskId: Int): Flow<Task?>

    @Query("SELECT * FROM task WHERE taskProjectId =:taskProjectId")
    fun getTaskByProjectId(taskProjectId:Int):Flow<List<Task>>

    @Query("SELECT COUNT(*) FROM task WHERE taskProjectId = :taskProjectId")
    fun getTotalTasks(taskProjectId: Int): Flow<Int>

    @Query("SELECT COUNT(*) FROM task WHERE taskProjectId =:taskProjectId AND isDone = true")
    fun getCompletedTasks(taskProjectId: Int): Flow<Int>

    @Query("SELECT * FROM task")
    fun getAllTasks(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTask(task: Task)

    @Query("DELETE FROM task WHERE taskId = :taskId")
    suspend fun deleteTask(taskId: Int)
}