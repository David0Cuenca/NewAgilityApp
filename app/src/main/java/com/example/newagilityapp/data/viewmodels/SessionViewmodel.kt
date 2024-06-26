package com.example.newagilityapp.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newagilityapp.data.repository.SessionRepository
import com.example.newagilityapp.model.Session
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {
    val getAllSessions: Flow<List<Session>> = sessionRepository.getAllSessions()

    fun deleteSession(session: Session) {
        viewModelScope.launch {
            sessionRepository.deleteSession(session)
        }
    }

    fun getSessionByProjectId(SessionProjectId: Int): Flow<List<Session>> {
        return sessionRepository.getSessionById(SessionProjectId)
    }

    fun getSessionById(sessionId: Int): Flow<List<Session>> {
        return sessionRepository.getSessionById(sessionId)
    }

    fun addOrUpdateSession(session: Session) {
        viewModelScope.launch {
            sessionRepository.addOrUpdateSession(session)
        }
    }
}