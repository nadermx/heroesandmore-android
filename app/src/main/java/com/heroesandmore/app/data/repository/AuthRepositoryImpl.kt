package com.heroesandmore.app.data.repository

import com.heroesandmore.app.data.api.AuthApi
import com.heroesandmore.app.data.dto.auth.*
import com.heroesandmore.app.data.local.TokenManager
import com.heroesandmore.app.domain.model.User
import com.heroesandmore.app.domain.repository.AuthRepository
import com.heroesandmore.app.util.Resource
import com.heroesandmore.app.util.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    private val _authStateFlow = MutableStateFlow(tokenManager.getAccessToken() != null)

    override suspend fun login(username: String, password: String): Resource<User> {
        val result = safeApiCall { authApi.login(LoginRequest(username, password)) }
        return when (result) {
            is Resource.Success -> {
                result.data?.let { tokenResponse ->
                    tokenManager.saveTokens(tokenResponse.access, tokenResponse.refresh)
                    _authStateFlow.value = true
                    // Fetch user profile after login
                    val profileResult = safeApiCall { authApi.getCurrentUser() }
                    when (profileResult) {
                        is Resource.Success -> {
                            profileResult.data?.let { profile ->
                                Resource.success(profile.toUser())
                            } ?: Resource.error("Failed to get user profile")
                        }
                        is Resource.Error -> Resource.error(profileResult.message ?: "Failed to get user profile")
                        is Resource.Loading -> Resource.loading()
                        else -> Resource.error("Unknown error")
                    }
                } ?: Resource.error("Login failed")
            }
            is Resource.Error -> Resource.error(result.message ?: "Login failed")
            is Resource.Loading -> Resource.loading()
            else -> Resource.error("Unknown error")
        }
    }

    override suspend fun register(username: String, email: String, password: String): Resource<User> {
        val result = safeApiCall { authApi.register(RegisterRequest(username, email, password, password)) }
        return when (result) {
            is Resource.Success -> {
                result.data?.let { _ ->
                    // Auto-login after registration
                    login(username, password)
                } ?: Resource.error("Registration failed")
            }
            is Resource.Error -> Resource.error(result.message ?: "Registration failed")
            is Resource.Loading -> Resource.loading()
            else -> Resource.error("Unknown error")
        }
    }

    override suspend fun refreshToken(): Resource<Boolean> {
        val refreshToken = tokenManager.getRefreshToken() ?: return Resource.error("No refresh token")
        val result = safeApiCall { authApi.refreshToken(RefreshTokenRequest(refreshToken)) }
        return when (result) {
            is Resource.Success -> {
                result.data?.let { tokenResponse ->
                    tokenManager.saveTokens(tokenResponse.access, tokenResponse.refresh)
                    Resource.success(true)
                } ?: Resource.error("Token refresh failed")
            }
            is Resource.Error -> {
                tokenManager.clearTokens()
                _authStateFlow.value = false
                Resource.error(result.message ?: "Token refresh failed")
            }
            is Resource.Loading -> Resource.loading()
            else -> Resource.error("Unknown error")
        }
    }

    override suspend fun logout() {
        tokenManager.clearTokens()
        _authStateFlow.value = false
    }

    override suspend fun googleAuth(idToken: String): Resource<User> {
        val result = safeApiCall { authApi.googleAuth(GoogleAuthRequest(idToken)) }
        return when (result) {
            is Resource.Success -> {
                result.data?.let { tokenResponse ->
                    tokenManager.saveTokens(tokenResponse.access, tokenResponse.refresh)
                    _authStateFlow.value = true
                    // Fetch user profile
                    val profileResult = safeApiCall { authApi.getCurrentUser() }
                    when (profileResult) {
                        is Resource.Success -> {
                            profileResult.data?.let { profile ->
                                Resource.success(profile.toUser())
                            } ?: Resource.error("Failed to get user profile")
                        }
                        is Resource.Error -> Resource.error(profileResult.message ?: "Failed to get user profile")
                        is Resource.Loading -> Resource.loading()
                        else -> Resource.error("Unknown error")
                    }
                } ?: Resource.error("Google auth failed")
            }
            is Resource.Error -> Resource.error(result.message ?: "Google auth failed")
            is Resource.Loading -> Resource.loading()
            else -> Resource.error("Unknown error")
        }
    }

    override suspend fun requestPasswordReset(email: String): Resource<Boolean> {
        val result = safeApiCall { authApi.requestPasswordReset(PasswordResetRequest(email)) }
        return when (result) {
            is Resource.Success -> Resource.success(true)
            is Resource.Error -> Resource.error(result.message ?: "Failed to request password reset")
            is Resource.Loading -> Resource.loading()
            else -> Resource.error("Unknown error")
        }
    }

    override suspend fun confirmPasswordReset(uid: String, token: String, newPassword: String): Resource<Boolean> {
        val result = safeApiCall { authApi.confirmPasswordReset(PasswordResetConfirmRequest(uid, token, newPassword, newPassword)) }
        return when (result) {
            is Resource.Success -> Resource.success(true)
            is Resource.Error -> Resource.error(result.message ?: "Failed to reset password")
            is Resource.Loading -> Resource.loading()
            else -> Resource.error("Unknown error")
        }
    }

    override suspend fun changePassword(oldPassword: String, newPassword: String): Resource<Boolean> {
        val result = safeApiCall { authApi.changePassword(ChangePasswordRequest(oldPassword, newPassword, newPassword)) }
        return when (result) {
            is Resource.Success -> Resource.success(true)
            is Resource.Error -> Resource.error(result.message ?: "Failed to change password")
            is Resource.Loading -> Resource.loading()
            else -> Resource.error("Unknown error")
        }
    }

    override fun isLoggedIn(): Boolean = tokenManager.getAccessToken() != null

    override fun getAuthStateFlow(): Flow<Boolean> = _authStateFlow

    private fun com.heroesandmore.app.data.dto.accounts.ProfileDto.toUser(): User {
        return User(
            id = id,
            username = username,
            email = email,
            avatarUrl = avatarUrl,
            bio = bio,
            location = location,
            website = website,
            isSellerVerified = isSellerVerified,
            isTrustedSeller = isTrustedSeller,
            rating = rating,
            stripeAccountComplete = stripeAccountComplete ?: false,
            created = created
        )
    }
}
