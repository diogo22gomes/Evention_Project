package com.example.evention.ui.screens.profile.user.changePassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evention.di.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
) : ViewModel() {

    private val userRemoteDataSource = NetworkModule.userRemoteDataSource

    private val _state = MutableStateFlow<ChangePasswordState>(ChangePasswordState.Idle)
    val state: StateFlow<ChangePasswordState> = _state

    fun changePassword(oldPassword: String, newPassword: String, confirmPassword: String) {
        if (oldPassword.isBlank() || newPassword.isBlank() || confirmPassword.isBlank()) {
            _state.value = ChangePasswordState.Error("All fields are required")
            return
        }

        if (newPassword != confirmPassword) {
            _state.value = ChangePasswordState.Error("Passwords do not match")
            return
        }

        viewModelScope.launch {
            _state.value = ChangePasswordState.Loading
            try {
                val result = userRemoteDataSource.changePassword(oldPassword, newPassword)
                result
                    .onSuccess {
                        _state.value = ChangePasswordState.Success("Password change successfully")
                    }
                    .onFailure { e ->
                        _state.value = ChangePasswordState.Error("Error changing Password, confirm old Password!")
                    }
            } catch (e: Exception) {
                _state.value = ChangePasswordState.Error("Error changing Password!")
            }
        }
    }

    fun resetState() {
        _state.value = ChangePasswordState.Idle
    }

    sealed class ChangePasswordState {
        object Idle : ChangePasswordState()
        object Loading : ChangePasswordState()
        data class Success(val message: String) : ChangePasswordState()
        data class Error(val message: String) : ChangePasswordState()
    }
}