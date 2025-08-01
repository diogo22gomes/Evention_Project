package com.example.evention.ui.screens.auth.login

import LoginRemoteDataSource
import LoginResponse
import UserPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await


class LoginScreenViewModel(
    private val loginRemoteDataSource: LoginRemoteDataSource,
    private val userPreferences: UserPreferences
) : ViewModel() {

    var loginState by mutableStateOf<LoginState>(LoginState.Idle)
        private set

    fun login(email: String, password: String) {
        viewModelScope.launch {
            loginState = LoginState.Loading
            try {
                val result = loginRemoteDataSource.login(email, password)

                if (result.isSuccess) {
                    val response = result.getOrThrow()
                    val token = response.token
                    val payload = decodeJWT(token)
                    val userGuid = payload.getString("userID")
                    val userType = payload.getString("userType")

                    userPreferences.saveToken(token)
                    userPreferences.saveUserId(userGuid)
                    userPreferences.saveUserType(userType)

                    val fcmToken = FirebaseMessaging.getInstance().token.await()

                    Firebase.firestore.collection("evention")
                        .document(userGuid)
                        .set(
                            mapOf(
                                "fcmToken" to fcmToken,
                                "updatedAt" to FieldValue.serverTimestamp()
                            )
                        ).await()

                    loginState = LoginState.Success(response)
                } else {
                    loginState = LoginState.Error(result.exceptionOrNull()?.message ?: "Erro desconhecido")
                }

            } catch (e: Exception) {
                Log.e("LoginViewModel", "Erro no login/Firebase: ${e.message}")
                loginState = LoginState.Error(e.message ?: "Erro inesperado")
            }
        }
    }


    fun resetState() {
        loginState = LoginState.Idle
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            loginState = LoginState.Loading
            try {
                val result = loginRemoteDataSource.loginWithGoogle(idToken)
                if (result.isSuccess) {
                    val response = result.getOrThrow()
                    val token = response.token
                    val payload = decodeJWT(token)
                    val userGuid = payload.getString("userID")

                    userPreferences.saveToken(token)
                    userPreferences.saveUserId(userGuid)

                    loginState = LoginState.Success(response)
                } else {
                    loginState = LoginState.Error("Erro no login com Google")
                }
            } catch (e: Exception) {
                loginState = LoginState.Error("Erro: ${e.message}")
            }
        }
    }


    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        data class Success(val response: LoginResponse) : LoginState()
        data class Error(val message: String) : LoginState()
    }
}
