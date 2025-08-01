package com.example.evention.data.remote.authentication

import GoogleLoginRequest
import LoginResponse
import android.util.Log

class RegisterRemoteDataSource(private val api: RegisterApiService) {

    suspend fun register(username: String, email: String, password: String): Result<RegisterResponse> {

        Log.d("RegisterRemote", "Attempting register API call")

        return try {
            val response = api.register(RegisterRequest(username, email, password))

            Log.d("RegisterRemote", "Response: $response")

            if (response.isSuccessful) {
                val body = response.body()

                Log.d("body", "$body")

                if (response.code() == 201) {
                    Result.success(RegisterResponse(success = true, message = "User registered successfully"))
                } else if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Resposta vazia"))
                }

            } else {
                Result.failure(Exception("Erro ${response.code()}: ${response.message()}"))
            }

        } catch (e: Exception) {
            Log.e("RegisterRemote", "Exception: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun registerWithGoogle(idToken: String): Result<LoginResponse> {
        return try {
            val response = api.registerWithGoogle(GoogleLoginRequest(idToken))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Erro: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
