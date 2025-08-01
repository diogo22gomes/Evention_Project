package com.example.evention.data.remote.authentication

import UserPreferences
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.evention.di.NetworkModule
import com.example.evention.ui.screens.auth.register.RegisterScreenViewModel

class RegisterViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val registerRemoteDataSource = NetworkModule.registerRemoteDataSource
        val userPreferences = UserPreferences(context)
        return RegisterScreenViewModel(registerRemoteDataSource, userPreferences) as T
    }
}
