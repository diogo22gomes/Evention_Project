package com.example.evention.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evention.model.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.evention.di.NetworkModule

class HomeScreenViewModel : ViewModel() {

    private val remoteDataSource = NetworkModule.eventRemoteDataSource

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    init {
        viewModelScope.launch {
            try {
                _events.value = remoteDataSource.getApprovedEvents()
            } catch (e: Exception) {
                Log.e("HomeScreenViewModel", "Erro ao obter eventos", e)
            }
        }
    }
}

