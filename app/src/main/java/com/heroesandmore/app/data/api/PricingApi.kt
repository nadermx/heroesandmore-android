package com.heroesandmore.app.data.api

import com.heroesandmore.app.data.dto.common.PaginatedResponse
import com.heroesandmore.app.data.dto.pricing.*
import retrofit2.Response
import retrofit2.http.*

interface PricingApi {

    @GET("pricing/items/")
    suspend fun getPriceGuideItems(
        @Query("page") page: Int = 1,
        @Query("category") category: String? = null,
        @Query("year") year: Int? = null,
        @Query("set") set: String? = null,
        @Query("search") search: String? = null,
        @Query("ordering") ordering: String? = null
    ): Response<PaginatedResponse<PriceGuideItemDto>>

    @GET("pricing/items/{id}/")
    suspend fun getPriceGuideItemById(@Path("id") id: Int): Response<PriceGuideItemDetailDto>

    @GET("pricing/items/{slug}/")
    suspend fun getPriceGuideItemBySlug(@Path("slug") slug: String): Response<PriceGuideItemDetailDto>

    @GET("pricing/items/{id}/grades/")
    suspend fun getGradePrices(@Path("id") itemId: Int): Response<List<GradePriceDto>>

    @GET("pricing/items/{id}/sales/")
    suspend fun getSaleRecords(
        @Path("id") itemId: Int,
        @Query("page") page: Int = 1
    ): Response<PaginatedResponse<SaleRecordDto>>

    @GET("pricing/items/{id}/history/")
    suspend fun getPriceHistory(@Path("id") itemId: Int): Response<List<PriceHistoryPointDto>>

    @GET("pricing/trending/")
    suspend fun getTrendingItems(): Response<List<PriceGuideItemDto>>

    @GET("pricing/items/search/")
    suspend fun searchPriceGuide(
        @Query("q") query: String
    ): Response<PaginatedResponse<PriceGuideItemDto>>

    @GET("pricing/categories/")
    suspend fun getCategories(): Response<List<PriceGuideCategoryDto>>
}
