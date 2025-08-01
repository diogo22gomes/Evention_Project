package com.example.evention.ui.screens.auth.register

import UserPreferences
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.viewModelScope
import com.example.evention.data.remote.authentication.RegisterRemoteDataSource
import com.example.evention.ui.screens.auth.login.decodeJWT
import kotlinx.coroutines.launch

class RegisterScreenViewModel(
    private val registerRemoteDataSource: RegisterRemoteDataSource,
    private val userPreferences: UserPreferences

) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun register(username: String, email: String, password: String, confirmPassword: String) {

        if (username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _registerState.value = RegisterState.Error("All fields are required")
            return
        }

        if (password != confirmPassword) {
            _registerState.value = RegisterState.Error("Passwords do not match")
            return
        }

        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            try {
                val result = registerRemoteDataSource.register(username, email, password)
                result
                    .onSuccess { response ->
                        if (response.success) {
                            _registerState.value = RegisterState.Success
                        } else {
                            val errorMessage = response.message ?: "Unknown error occurred"
                            _registerState.value = RegisterState.Error(errorMessage)
                        }
                    }
                    .onFailure { e ->
                        _registerState.value = RegisterState.Error("Erro: ${e.message}")
                    }
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error("Erro inesperado: ${e.message}")
            }
        }
    }


    fun resetState() {
        _registerState.value = RegisterState.Idle
    }

    fun registerWithGoogle(idToken: String) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            try {
                val result = registerRemoteDataSource.registerWithGoogle(idToken)
                if (result.isSuccess) {
                    val response = result.getOrThrow()
                    val token = response.token
                    val payload = decodeJWT(token)
                    val userGuid = payload.getString("userID")

                    userPreferences.saveToken(token)
                    userPreferences.saveUserId(userGuid)

                    _registerState.value = RegisterState.Success
                } else {
                    _registerState.value = RegisterState.Error("Erro ao registar com Google")
                }
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error("Erro: ${e.message}")
            }
        }
    }

    sealed class RegisterState {
        object Idle : RegisterState()
        object Loading : RegisterState()
        object Success : RegisterState()
        data class Error(val message: String) : RegisterState()
    }
}

