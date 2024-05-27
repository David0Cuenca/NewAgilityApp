package com.example.newagilityapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.newagilityapp.model.Session
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Insert
    suspend fun insertSession(session: Session)

    @Delete
    suspend fun deleteSession(session: Session)

    @Query("SELECT * FROM Session")
    fun getAllSessions(): Flow<List<Session>>

    @Query("SELECT * FROM Session WHERE projectSessionId = :projectId")
    fun getSessionById(projectId:Int): Flow<List<Session>>

    @Query("SELECT SUM(duration) FROM SESSION")
    fun getTotalSessionsDuration():Flow<Long>

    @Query("SELECT * FROM SESSION WHERE projectSessionId=:projectId")
    fun getSessionByProjectId(projectId: Int): Flow<List<Session>>

    @Query("SELECT SUM(duration) FROM SESSION WHERE projectSessionId=:projectId")
    fun getTotalSessionsDurationByProjectId(projectId: Int):Flow<Long>

    @Query("DELETE FROM SESSION WHERE projectSessionId=:projectId")
    fun deleteSessionsByProjectId(projectId: Int)
}