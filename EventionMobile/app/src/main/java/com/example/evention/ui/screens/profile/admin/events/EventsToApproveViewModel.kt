package com.example.evention.ui.screens.profile.admin.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evention.di.NetworkModule
import com.example.evention.model.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventsToApproveViewModel : ViewModel() {

    private val remoteDataSource = NetworkModule.eventRemoteDataSource

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    private val _approveSuccess = MutableStateFlow(false)
    val approveSuccess: StateFlow<Boolean> = _approveSuccess

    private val _deleteSuccess = MutableStateFlow(false)
    val deleteSuccess: StateFlow<Boolean> = _deleteSuccess

    init {
        viewModelScope.launch {
            try {
                _events.value = remoteDataSource.getSuspendedEvents()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun approveEvent(eventId: String) {
        viewModelScope.launch {
            try {
                val approvedEvent = remoteDataSource.approveEvent(eventId)
                _events.value = _events.value.filter { it.eventID != approvedEvent.eventID }
                _approveSuccess.value = true
            } catch (e: Exception) {
                _approveSuccess.value = false
            }
        }
    }

    fun deleteEvent(eventId: String) {
        viewModelScope.launch {
            try {
                remoteDataSource.deleteEvent(eventId)
                _events.value = _events.value.filter { it.eventID != eventId }
                _deleteSuccess.value = true
            } catch (e: Exception) {
                _deleteSuccess.value = false
            }
        }
    }

    fun clearApproveSuccess() {
        _approveSuccess.value = false
    }

    fun clearDeleteSuccess() {
        _deleteSuccess.value = false
    }
}
