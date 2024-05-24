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
class SessionViewModel @Inject constructor (private val sessionRepository: SessionRepository) : ViewModel() {

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