data class LoginResponse(
    val token: String,
    val userGuid: String,
)

data class ResetPasswordResponse(
    val success: Boolean,
    val message: String?
)

data class ConfirmPasswordResponse(
    val success: Boolean,
    val message: String?
)

