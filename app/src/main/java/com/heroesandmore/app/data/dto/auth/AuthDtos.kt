package com.heroesandmore.app.data.dto.auth

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    @SerializedName("password_confirm")
    val passwordConfirm: String
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
    val uid: String,
    val token: String,
    @SerializedName("new_password")
    val newPassword: String,
    @SerializedName("new_password_confirm")
    val newPasswordConfirm: String
)

data class GoogleAuthRequest(
    @SerializedName("id_token")
    val idToken: String
)

data class ChangePasswordRequest(
    @SerializedName("old_password")
    val oldPassword: String,
    @SerializedName("new_password")
    val newPassword: String,
    @SerializedName("new_password_confirm")
    val newPasswordConfirm: String
)
