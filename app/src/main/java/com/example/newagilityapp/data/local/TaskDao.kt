package com.example.newagilityapp.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.newagilityapp.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Upsert
    suspend fun upsertTask(task: Task)

    @Query("DELETE FROM task WHERE taskId =:taskId")
    suspend fun deleteTas(taskId:Int)

    @Query("DELETE FROM task WHERE taskProjectId=:projectId")
    suspend fun deleteTaskByProjectId(projectId:Int)

    @Query("SELECT * FROM task WHERE taskId=:taskId")
    suspend fun getTaskById(taskId: Int): Task?

    @Query("SELECT * FROM task WHERE taskProjectId=:projectId")
    fun getTaskFromProject(projectId: Int):Flow<List<Task>>

    @Query("SELECT * FROM task")
    fun getAllTasks():Flow<List<Task>>

}