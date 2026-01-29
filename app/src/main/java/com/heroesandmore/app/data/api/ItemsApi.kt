package com.heroesandmore.app.data.api

import com.heroesandmore.app.data.dto.common.PaginatedResponse
import com.heroesandmore.app.data.dto.items.*
import com.heroesandmore.app.data.dto.marketplace.ListingDto
import retrofit2.Response
import retrofit2.http.*

interface ItemsApi {

    @GET("items/categories/")
    suspend fun getCategoryTree(): Response<List<CategoryDto>>

    @GET("items/categories/list/")
    suspend fun getCategoryList(): Response<List<CategoryListDto>>

    @GET("items/categories/{slug}/")
    suspend fun getCategory(@Path("slug") slug: String): Response<CategoryDto>

    @GET("items/categories/{slug}/listings/")
    suspend fun getCategoryListings(
        @Path("slug") slug: String,
        @Query("page") page: Int = 1
    ): Response<PaginatedResponse<ListingDto>>

    @GET("items/search/")
    suspend fun globalSearch(
        @Query("q") query: String
    ): Response<List<SearchResultDto>>

    @GET("items/autocomplete/")
    suspend fun autocomplete(
        @Query("q") query: String
    ): Response<List<AutocompleteDto>>
}
