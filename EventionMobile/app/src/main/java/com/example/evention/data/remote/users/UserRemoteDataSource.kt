package com.example.evention.data.remote.users

import com.example.evention.model.ChangePasswordRequest
import com.example.evention.model.User
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class UserRemoteDataSource(private val api: UserApiService) {
    suspend fun getUsers(): List<User> = api.getUsers()

    suspend fun getUserProfile(): User = api.getUserProfile()

    suspend fun getUserById(userId: String) = api.getUserById(userId)

    suspend fun deleteUser(userId: String) = api.deleteUser(userId)

    suspend fun updateUser(
        userId: String,
        username: String,
        email: String,
        phone: Int,
        profilePicture: MultipartBody.Part? = null
    ): User {
        val usernamePart = username.toRequestBody("text/plain".toMediaType())
        val emailPart = email.toRequestBody("text/plain".toMediaType())
        val phonePart = phone.toString().toRequestBody("text/plain".toMediaType())

        return api.updateUser(
            userId = userId,
            username = usernamePart,
            email = emailPart,
            phone = phonePart,
            profilePicture = profilePicture
        )
    }

    suspend fun changePassword(oldPassword: String, newPassword: String): Result<Unit> {
        return try {
            val request = ChangePasswordRequest(oldPassword, newPassword)
            api.changePassword(request)
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(Exception("HTTP error: ${e.code()} ${e.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
