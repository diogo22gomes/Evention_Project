package com.example.evention.ui.screens.profile.user.userProfile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evention.di.NetworkModule
import com.example.evention.model.Event
import com.example.evention.model.Reputation
import com.example.evention.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserProfileViewModel : ViewModel() {

    private val remoteDataSource = NetworkModule.userRemoteDataSource
    private val eventRemoteDataSource = NetworkModule.eventRemoteDataSource

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _reputation = MutableStateFlow<Reputation?>(null)
    val reputation: StateFlow<Reputation?> = _reputation

    private val _events = MutableStateFlow<Map<String, Event>>(emptyMap())
    val events: StateFlow<Map<String, Event>> = _events

    fun loadUserProfile() {
        viewModelScope.launch {
            try {
                val fetchedUser = remoteDataSource.getUserProfile()
                _user.value = fetchedUser
            } catch (e: Exception) {
                _user.value = null
            }
        }
    }

    fun loadUserReputation(userId: String) {
        viewModelScope.launch {
            try {
                val fetchedReputation = eventRemoteDataSource.getUserReputation(userId)
                _reputation.value = fetchedReputation
            } catch (e: Exception) {
                _reputation.value = null
            }
        }
    }

    fun loadEventById(eventId: String) {
        viewModelScope.launch {
            try {
                val fetchedEvent = eventRemoteDataSource.getEventById(eventId)
                _events.value = _events.value.toMutableMap().apply {
                    put(eventId, fetchedEvent)
                }
            } catch (e: Exception) {
                Log.e("UserProfileViewModel", "Error loading event $eventId", e)
            }
        }
    }
}
