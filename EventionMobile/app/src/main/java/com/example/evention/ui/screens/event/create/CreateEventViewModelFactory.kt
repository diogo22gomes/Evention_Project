package com.example.evention.ui.screens.event.create

import UserPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CreateEventViewModelFactory(
    private val userPreferences: UserPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateEventViewModel::class.java)) {
            return CreateEventViewModel(userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
