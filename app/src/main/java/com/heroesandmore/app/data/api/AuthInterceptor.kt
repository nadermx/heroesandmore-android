package com.heroesandmore.app.data.api

import com.heroesandmore.app.data.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Skip auth for login/register endpoints
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

                // TODO: Implement token refresh logic here
                // For now, just clear tokens and let user re-login
                // tokenManager.clearTokens()
            }
        }

        return response
    }
}
