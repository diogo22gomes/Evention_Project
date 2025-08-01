package com.example.evention.ui.screens.auth.confirmpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evention.di.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ConfirmPasswordViewModel(
) : ViewModel() {

    private val resetPasswordRemoteDataSource = NetworkModule.resetPasswordRemoteDataSource

    private val _state = MutableStateFlow<ConfirmPasswordState>(ConfirmPasswordState.Idle)
    val state: StateFlow<ConfirmPasswordState> = _state

    fun confirmPassword(token: String, newPassword: String, confirmPassword: String) {
        if (token.isBlank() || newPassword.isBlank() || confirmPassword.isBlank()) {
            _state.value = ConfirmPasswordState.Error("All fields are required")
            return
        }

        if (newPassword != confirmPassword) {
            _state.value = ConfirmPasswordState.Error("Passwords do not match")
            return
        }

        viewModelScope.launch {
            _state.value = ConfirmPasswordState.Loading
            try {
                val result = resetPasswordRemoteDataSource.confirmPassword(token, newPassword)
                result
                    .onSuccess {
                        _state.value = ConfirmPasswordState.Success("Password reset successfully")
                    }
                    .onFailure { e ->
                        _state.value = ConfirmPasswordState.Error("Error: ${e.message}")
                    }
            } catch (e: Exception) {
                _state.value = ConfirmPasswordState.Error("Unexpected error: ${e.message}")
            }
        }
    }

    fun resetState() {
        _state.value = ConfirmPasswordState.Idle
    }

    sealed class ConfirmPasswordState {
        object Idle : ConfirmPasswordState()
        object Loading : ConfirmPasswordState()
        data class Success(val message: String) : ConfirmPasswordState()
        data class Error(val message: String) : ConfirmPasswordState()
    }
}
