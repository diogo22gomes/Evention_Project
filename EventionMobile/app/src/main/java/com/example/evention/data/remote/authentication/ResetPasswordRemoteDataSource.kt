package com.example.evention.data.remote.authentication

import ConfirmPasswordRequest
import LoginApiService
import ResetPasswordRequest
import ResetPasswordResponse

class ResetPasswordRemoteDataSource(private val api: LoginApiService) {

    suspend fun resetPassword(email: String): Result<ResetPasswordResponse> {
        return try {
            val response = api.resetPassword(ResetPasswordRequest(email))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Erro: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun confirmPassword(token: String, newPassword: String): Result<Unit> {
        return try {
            val response = api.confirmPassword(ConfirmPasswordRequest(token, newPassword))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
