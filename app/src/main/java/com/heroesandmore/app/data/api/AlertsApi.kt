package com.heroesandmore.app.data.api

import com.heroesandmore.app.data.dto.alerts.*
import com.heroesandmore.app.data.dto.common.PaginatedResponse
import com.heroesandmore.app.data.dto.marketplace.StatusResponse
import retrofit2.Response
import retrofit2.http.*

interface AlertsApi {

    // Notifications
    @GET("alerts/notifications/")
    suspend fun getNotifications(
        @Query("page") page: Int = 1
    ): Response<PaginatedResponse<AlertDto>>

    @PATCH("alerts/notifications/{id}/read/")
    suspend fun markAsRead(@Path("id") alertId: Int): Response<StatusResponse>

    @POST("alerts/notifications/read-all/")
    suspend fun markAllAsRead(): Response<StatusResponse>

    // Wishlists
    @GET("alerts/wishlists/")
    suspend fun getWishlists(): Response<List<WishlistDto>>

    @POST("alerts/wishlists/")
    suspend fun createWishlist(@Body request: CreateWishlistRequest): Response<WishlistDto>

    @GET("alerts/wishlists/{id}/")
    suspend fun getWishlist(@Path("id") id: Int): Response<WishlistDetailDto>

    @PATCH("alerts/wishlists/{id}/")
    suspend fun updateWishlist(
        @Path("id") id: Int,
        @Body request: UpdateWishlistRequest
    ): Response<WishlistDto>

    @DELETE("alerts/wishlists/{id}/")
    suspend fun deleteWishlist(@Path("id") id: Int): Response<Unit>

    @POST("alerts/wishlists/{id}/items/")
    suspend fun addWishlistItem(
        @Path("id") wishlistId: Int,
        @Body request: CreateWishlistItemRequest
    ): Response<WishlistItemDto>

    @DELETE("alerts/wishlists/{wishlistId}/items/{itemId}/")
    suspend fun deleteWishlistItem(
        @Path("wishlistId") wishlistId: Int,
        @Path("itemId") itemId: Int
    ): Response<Unit>

    // Saved Searches
    @GET("alerts/saved-searches/")
    suspend fun getSavedSearches(): Response<List<SavedSearchDto>>

    @POST("alerts/saved-searches/")
    suspend fun createSavedSearch(@Body request: CreateSavedSearchRequest): Response<SavedSearchDto>

    @DELETE("alerts/saved-searches/{id}/")
    suspend fun deleteSavedSearch(@Path("id") id: Int): Response<Unit>

    @GET("alerts/saved-searches/{id}/matches/")
    suspend fun getSavedSearchMatches(@Path("id") id: Int): Response<List<com.heroesandmore.app.data.dto.marketplace.ListingDto>>

    // Price Alerts
    @GET("alerts/price-alerts/")
    suspend fun getPriceAlerts(): Response<List<PriceAlertDto>>

    @POST("alerts/price-alerts/")
    suspend fun createPriceAlert(@Body request: CreatePriceAlertRequest): Response<PriceAlertDto>

    @PATCH("alerts/price-alerts/{id}/")
    suspend fun updatePriceAlert(
        @Path("id") id: Int,
        @Body request: UpdatePriceAlertRequest
    ): Response<PriceAlertDto>

    @DELETE("alerts/price-alerts/{id}/")
    suspend fun deletePriceAlert(@Path("id") id: Int): Response<Unit>
}
