import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApiService {
    @POST("/user/api/users/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/user/api/users/loginG")
    suspend fun loginWithGoogle(@Body request: GoogleLoginRequest): Response<LoginResponse>

    @POST("/user/api/users/send-reset-token")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ResetPasswordResponse>

    @POST("/user/api/users/reset-password")
    suspend fun confirmPassword(@Body request: ConfirmPasswordRequest): Response<ConfirmPasswordResponse>

}

