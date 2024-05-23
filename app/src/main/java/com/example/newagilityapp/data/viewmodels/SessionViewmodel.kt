package com.example.newagilityapp.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newagilityapp.data.repository.SessionRepository
import com.example.newagilityapp.model.Session
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SessionViewModel(private val sessionRepository: SessionRepository) : ViewModel() {

    val allSessions: Flow<List<Session>> = sessionRepository.getAllSessions()
    val totalSessionsDuration: Flow<Long> = sessionRepository.getTotalSessionsDuration()

    fun addSession(session: Session) {
        viewModelScope.launch {
            sessionRepository.addSession(session)
        }
    }

    fun deleteSession(session: Session) {
        viewModelScope.launch {
            sessionRepository.deleteSession(session)
        }
    }
}