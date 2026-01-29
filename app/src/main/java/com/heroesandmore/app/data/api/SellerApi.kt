package com.heroesandmore.app.data.api

import com.heroesandmore.app.data.dto.common.PaginatedResponse
import com.heroesandmore.app.data.dto.marketplace.ListingDetailDto
import com.heroesandmore.app.data.dto.marketplace.OrderDto
import com.heroesandmore.app.data.dto.seller.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface SellerApi {

    @GET("seller/dashboard/")
    suspend fun getDashboard(): Response<DashboardStatsDto>

    @GET("seller/analytics/")
    suspend fun getAnalytics(
        @Query("days") days: Int = 30
    ): Response<AnalyticsDto>

    @GET("seller/subscription/")
    suspend fun getSubscription(): Response<SubscriptionDto>

    // Inventory
    @GET("seller/inventory/")
    suspend fun getInventory(
        @Query("page") page: Int = 1,
        @Query("search") search: String? = null,
        @Query("ordering") ordering: String? = null
    ): Response<PaginatedResponse<InventoryItemDto>>

    @POST("seller/inventory/")
    suspend fun addInventoryItem(@Body request: CreateInventoryItemRequest): Response<InventoryItemDto>

    @PATCH("seller/inventory/{id}/")
    suspend fun updateInventoryItem(
        @Path("id") id: Int,
        @Body request: UpdateInventoryItemRequest
    ): Response<InventoryItemDto>

    @DELETE("seller/inventory/{id}/")
    suspend fun deleteInventoryItem(@Path("id") id: Int): Response<Unit>

    @POST("seller/inventory/{id}/create_listing/")
    suspend fun createListingFromInventory(@Path("id") id: Int): Response<ListingDetailDto>

    // Bulk Imports
    @GET("seller/imports/")
    suspend fun getBulkImports(
        @Query("page") page: Int = 1
    ): Response<PaginatedResponse<BulkImportDto>>

    @Multipart
    @POST("seller/imports/")
    suspend fun createBulkImport(
        @Part file: MultipartBody.Part,
        @Part("auto_publish") autoPublish: Boolean = false,
        @Part("default_category") defaultCategory: Int? = null
    ): Response<BulkImportDto>

    @GET("seller/imports/{id}/")
    suspend fun getBulkImport(@Path("id") id: Int): Response<BulkImportDto>

    @GET("seller/imports/{id}/rows/")
    suspend fun getBulkImportRows(@Path("id") id: Int): Response<List<BulkImportRowDto>>

    // Orders
    @GET("seller/orders/")
    suspend fun getSellerOrders(
        @Query("page") page: Int = 1
    ): Response<PaginatedResponse<OrderDto>>

    @GET("seller/sales/")
    suspend fun getSalesHistory(
        @Query("page") page: Int = 1
    ): Response<PaginatedResponse<OrderDto>>
}
