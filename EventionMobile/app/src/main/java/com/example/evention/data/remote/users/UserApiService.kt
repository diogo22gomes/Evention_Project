package com.example.evention.data.remote.users

import com.example.evention.model.ChangePasswordRequest
import com.example.evention.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface UserApiService {
    @GET("user/api/users")
    suspend fun getUsers(): List<User>

    @GET("user/api/users/my-profile")
    suspend fun getUserProfile(): User

    @GET("user/api/users/{id}")
    suspend fun getUserById(@Path("id") userId: String): User

    @DELETE("user/api/users/{id}")
    suspend fun deleteUser(@Path("id") userId: String)

    @Multipart
    @PUT("user/api/users/{id}")
    suspend fun updateUser(
        @Path("id") userId: String,
        @Part("username") username: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part profilePicture: MultipartBody.Part? = null
    ): User

    @PUT("user/api/users/change-password")
    suspend fun changePassword(
        @Body changePasswordRequest: ChangePasswordRequest
    )
}