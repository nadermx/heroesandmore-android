package com.heroesandmore.app.data.api

import com.heroesandmore.app.data.dto.accounts.*
import com.heroesandmore.app.data.dto.auth.*
import com.heroesandmore.app.data.dto.collections.CollectionDto
import com.heroesandmore.app.data.dto.common.PaginatedResponse
import com.heroesandmore.app.data.dto.marketplace.ListingDto
import com.heroesandmore.app.data.dto.marketplace.ReviewDto
import com.heroesandmore.app.data.dto.marketplace.StatusResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {

    @POST("auth/token/")
    suspend fun login(@Body request: LoginRequest): Response<TokenResponse>

    @POST("auth/token/refresh/")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<TokenResponse>

    @POST("accounts/register/")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @GET("accounts/me/")
    suspend fun getCurrentUser(): Response<ProfileDto>

    @PATCH("accounts/me/")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<ProfileDto>

    @Multipart
    @POST("accounts/me/avatar/")
    suspend fun uploadAvatar(@Part avatar: MultipartBody.Part): Response<ProfileDto>

    @GET("accounts/me/notifications/")
    suspend fun getNotificationSettings(): Response<NotificationSettingsDto>

    @PATCH("accounts/me/notifications/")
    suspend fun updateNotificationSettings(@Body request: UpdateNotificationSettingsRequest): Response<NotificationSettingsDto>

    @GET("accounts/users/{username}/")
    suspend fun getPublicProfile(@Path("username") username: String): Response<PublicProfileDto>

    @GET("accounts/users/{username}/listings/")
    suspend fun getUserListings(
        @Path("username") username: String,
        @Query("page") page: Int = 1
    ): Response<PaginatedResponse<ListingDto>>

    @GET("accounts/users/{username}/collections/")
    suspend fun getUserCollections(@Path("username") username: String): Response<List<CollectionDto>>

    @GET("accounts/users/{username}/reviews/")
    suspend fun getUserReviews(
        @Path("username") username: String,
        @Query("page") page: Int = 1
    ): Response<PaginatedResponse<ReviewDto>>

    @GET("accounts/me/recently-viewed/")
    suspend fun getRecentlyViewed(): Response<List<RecentlyViewedDto>>

    @DELETE("accounts/me/recently-viewed/")
    suspend fun clearRecentlyViewed(): Response<StatusResponse>

    @POST("accounts/me/devices/")
    suspend fun registerDeviceToken(@Body request: DeviceTokenRequest): Response<StatusResponse>

    @DELETE("accounts/me/devices/{token}/")
    suspend fun removeDeviceToken(@Path("token") token: String): Response<StatusResponse>

    @POST("auth/google/")
    suspend fun googleAuth(@Body request: GoogleAuthRequest): Response<TokenResponse>

    @POST("auth/password/reset/")
    suspend fun requestPasswordReset(@Body request: PasswordResetRequest): Response<StatusResponse>

    @POST("auth/password/reset/confirm/")
    suspend fun confirmPasswordReset(@Body request: PasswordResetConfirmRequest): Response<StatusResponse>

    @POST("auth/password/change/")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<StatusResponse>
}
