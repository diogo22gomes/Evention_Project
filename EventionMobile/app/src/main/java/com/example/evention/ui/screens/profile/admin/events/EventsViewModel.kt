package com.example.evention.ui.screens.profile.admin.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evention.di.NetworkModule
import com.example.evention.model.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventsViewModel : ViewModel() {

    private val remoteDataSource = NetworkModule.eventRemoteDataSource

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    private val _deleteSuccess = MutableStateFlow(false)
    val deleteSuccess: StateFlow<Boolean> = _deleteSuccess

    init {
        viewModelScope.launch {
            try {
                _events.value = remoteDataSource.getEvents()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadEvents() {
        viewModelScope.launch {
            try {
                _events.value = remoteDataSource.getEvents()
            } catch (e: Exception) {
                e.printStackTrace()
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

    fun clearDeleteSuccess() {
        _deleteSuccess.value = false
    }
}
