package com.example.evention.ui.screens.profile.userEvents

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evention.di.NetworkModule
import com.example.evention.model.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserEventsViewModel : ViewModel() {

    private val remoteDataSource = NetworkModule.eventRemoteDataSource

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    init {
        loadUserEvents()
    }

    fun loadUserEvents() {
        viewModelScope.launch {
            try {
                _events.value = remoteDataSource.getMyEvents()
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
            } catch (e: Exception) {
                Log.e("Error", "Error deleting event!")
            }
        }
    }
}
