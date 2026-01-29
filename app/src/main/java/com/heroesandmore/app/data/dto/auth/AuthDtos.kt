package com.heroesandmore.app.data.dto.auth

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

data class TokenResponse(
    val access: String,
    val refresh: String
)

data class RefreshTokenRequest(
    val refresh: String
)

data class PasswordResetRequest(
    val email: String
)

data class PasswordResetConfirmRequest(
    val token: String,
    @SerializedName("new_password")
    val newPassword: String
)

data class GoogleAuthRequest(
    @SerializedName("id_token")
    val idToken: String
)

data class ChangePasswordRequest(
    @SerializedName("old_password")
    val oldPassword: String,
    @SerializedName("new_password")
    val newPassword: String
)
