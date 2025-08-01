package com.example.evention.ui.screens.auth.resetpassword

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evention.data.remote.authentication.ResetPasswordRemoteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    private val resetPasswordRemoteDataSource: ResetPasswordRemoteDataSource
) : ViewModel() {

    private val _state = MutableStateFlow<ResetPasswordState>(ResetPasswordState.Idle)
    val state: StateFlow<ResetPasswordState> = _state

    fun resetPassword(email: String) {
        if (email.isBlank()) {
            _state.value = ResetPasswordState.Error("Email is required")
            return
        }

        viewModelScope.launch {
            _state.value = ResetPasswordState.Loading
            try {
                val result = resetPasswordRemoteDataSource.resetPassword(email)
                Log.d("resutl", "$result")
                result.onSuccess {
                    _state.value = ResetPasswordState.Success(it.message ?: "Email sent")
                }.onFailure {
                    _state.value = ResetPasswordState.Error(it.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                _state.value = ResetPasswordState.Error("Exception: ${e.message}")
            }
        }
    }

    fun resetState() {
        _state.value = ResetPasswordState.Idle
    }

    sealed class ResetPasswordState {
        object Idle : ResetPasswordState()
        object Loading : ResetPasswordState()
        data class Success(val message: String) : ResetPasswordState()
        data class Error(val message: String) : ResetPasswordState()
    }
}
