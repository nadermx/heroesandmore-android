package com.heroesandmore.app.domain.repository

import com.heroesandmore.app.domain.model.User
import com.heroesandmore.app.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(username: String, password: String): Resource<User>
    suspend fun register(username: String, email: String, password: String): Resource<User>
    suspend fun refreshToken(): Resource<Boolean>
    suspend fun logout()
    suspend fun googleAuth(idToken: String): Resource<User>
    suspend fun requestPasswordReset(email: String): Resource<Boolean>
    suspend fun confirmPasswordReset(uid: String, token: String, newPassword: String): Resource<Boolean>
    suspend fun changePassword(oldPassword: String, newPassword: String): Resource<Boolean>
    fun isLoggedIn(): Boolean
    fun getAuthStateFlow(): Flow<Boolean>
}
