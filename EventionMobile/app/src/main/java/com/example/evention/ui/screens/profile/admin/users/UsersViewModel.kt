package com.example.evention.ui.screens.profile.admin.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evention.di.NetworkModule
import com.example.evention.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UsersViewModel : ViewModel() {

    private val remoteDataSource = NetworkModule.userRemoteDataSource

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val _deleteSuccess = MutableStateFlow(false)
    val deleteSuccess: StateFlow<Boolean> = _deleteSuccess

    init {
        viewModelScope.launch {
            try {
                _users.value = remoteDataSource.getUsers()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteUser(userId: String) {
        viewModelScope.launch {
            try {
                remoteDataSource.deleteUser(userId)
                _users.value = _users.value.filter { it.userID != userId }
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