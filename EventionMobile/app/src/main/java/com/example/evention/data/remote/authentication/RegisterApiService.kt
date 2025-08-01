package com.example.evention.data.remote.authentication

import GoogleLoginRequest
import LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterApiService {
    @POST("/user/api/users")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("/user/api/users/createG")
    suspend fun registerWithGoogle(@Body request: GoogleLoginRequest): Response<LoginResponse>
}