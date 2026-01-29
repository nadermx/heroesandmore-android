package com.heroesandmore.app.data.api

import com.heroesandmore.app.data.dto.auth.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/token/")
    suspend fun login(@Body request: LoginRequest): Response<TokenResponse>

    @POST("auth/token/refresh/")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<TokenResponse>

    @POST("accounts/register/")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>
}
