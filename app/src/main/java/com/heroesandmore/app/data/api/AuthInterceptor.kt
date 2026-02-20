package com.heroesandmore.app.data.api

import com.heroesandmore.app.BuildConfig
import com.heroesandmore.app.data.local.TokenManager
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    private val gson = Gson()

    // Separate client for refresh calls to avoid interceptor loop
    private val refreshClient = OkHttpClient.Builder().build()

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Skip auth for login/register/refresh endpoints
        if (originalRequest.url.encodedPath.contains("/auth/token") ||
            originalRequest.url.encodedPath.contains("/register")) {
            return chain.proceed(originalRequest)
        }

        val token = tokenManager.getAccessToken()

        val request = if (token != null) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        val response = chain.proceed(request)

        // Handle 401 - token expired
        if (response.code == 401 && token != null) {
            synchronized(this) {
                // Check if token was already refreshed by another thread
                val currentToken = tokenManager.getAccessToken()
                if (currentToken != null && currentToken != token) {
                    // Token was refreshed, retry with new token
                    response.close()
                    val newRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer $currentToken")
                        .build()
                    return chain.proceed(newRequest)
                }

                // Attempt token refresh
                val refreshToken = tokenManager.getRefreshToken()
                if (refreshToken != null) {
                    val newAccessToken = attemptTokenRefresh(refreshToken)
                    if (newAccessToken != null) {
                        // Refresh succeeded, retry original request
                        response.close()
                        val newRequest = originalRequest.newBuilder()
                            .header("Authorization", "Bearer $newAccessToken")
                            .build()
                        return chain.proceed(newRequest)
                    }
                }

                // Refresh failed or no refresh token — clear tokens to force re-login
                tokenManager.clearTokens()
            }
        }

        return response
    }

    private fun attemptTokenRefresh(refreshToken: String): String? {
        return try {
            val body = gson.toJson(mapOf("refresh" to refreshToken))
                .toRequestBody("application/json".toMediaType())

            val request = Request.Builder()
                .url("${BuildConfig.API_BASE_URL}auth/token/refresh/")
                .post(body)
                .build()

            val response = refreshClient.newCall(request).execute()

            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                if (responseBody != null) {
                    val tokens = gson.fromJson(responseBody, TokenRefreshResponse::class.java)
                    tokenManager.saveTokens(tokens.access, tokens.refresh ?: refreshToken)
                    tokens.access
                } else null
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    private data class TokenRefreshResponse(
        val access: String,
        val refresh: String?
    )
}
