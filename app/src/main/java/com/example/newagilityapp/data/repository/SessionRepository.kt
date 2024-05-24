package com.example.newagilityapp.data.repository

import com.example.newagilityapp.data.local.SessionDao
import com.example.newagilityapp.model.Session
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SessionRepository @Inject constructor(private val sessionDao: SessionDao) {

    fun getAllSessions(): Flow<List<Session>> = sessionDao.getAllSessions()

    fun getTotalSessionsDuration(): Flow<Long> = sessionDao.getTotalSessionsDuration()

    suspend fun addSession(session: Session) {
        sessionDao.insertSession(session)
    }

    suspend fun deleteSession(session: Session) {
        sessionDao.deleteSession(session)
    }
}