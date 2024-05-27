package com.example.newagilityapp.data.repository

import com.example.newagilityapp.data.local.SessionDao
import com.example.newagilityapp.model.Session
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SessionRepository @Inject constructor(private val sessionDao: SessionDao) {
    fun getSessionById(sessionId: Int): Flow<List<Session>> = sessionDao.getSessionById(sessionId)
    fun getAllSessions(): Flow<List<Session>> = sessionDao.getAllSessions()

    fun getSessionsByProjectId(sessionProjectId:Int): Flow<List<Session>> = sessionDao.getSessionByProjectId(sessionProjectId)

    fun getTotalSessionsDuration(): Flow<Long> = sessionDao.getTotalSessionsDuration()
    fun getTotalSessionsDurationByProjectId(projectId: Int): Flow<Long> = sessionDao.getTotalSessionsDurationByProjectId(projectId)
    suspend fun addOrUpdateSession(session: Session) = sessionDao.insertSession(session)
    suspend fun deleteSession(session: Session) = sessionDao.deleteSession(session)
    suspend fun deleteSessionsByProjectId(projectId: Int) = sessionDao.deleteSessionsByProjectId(projectId)
}