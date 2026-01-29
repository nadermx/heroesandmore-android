package com.heroesandmore.app.data.api

import com.heroesandmore.app.data.dto.collections.*
import com.heroesandmore.app.data.dto.common.PaginatedResponse
import retrofit2.Response
import retrofit2.http.*

interface CollectionsApi {

    @GET("collections/mine/")
    suspend fun getMyCollections(): Response<List<CollectionDto>>

    @GET("collections/{id}/")
    suspend fun getCollection(@Path("id") id: Int): Response<CollectionDto>

    @POST("collections/")
    suspend fun createCollection(@Body request: CreateCollectionRequest): Response<CollectionDto>

    @PATCH("collections/{id}/")
    suspend fun updateCollection(
        @Path("id") id: Int,
        @Body request: UpdateCollectionRequest
    ): Response<CollectionDto>

    @DELETE("collections/{id}/")
    suspend fun deleteCollection(@Path("id") id: Int): Response<Unit>

    @GET("collections/{id}/items/")
    suspend fun getCollectionItems(@Path("id") collectionId: Int): Response<List<CollectionItemDto>>

    @POST("collections/{id}/items/")
    suspend fun addCollectionItem(
        @Path("id") collectionId: Int,
        @Body request: AddCollectionItemRequest
    ): Response<CollectionItemDto>

    @PATCH("collections/{collectionId}/items/{itemId}/")
    suspend fun updateCollectionItem(
        @Path("collectionId") collectionId: Int,
        @Path("itemId") itemId: Int,
        @Body request: UpdateCollectionItemRequest
    ): Response<CollectionItemDto>

    @DELETE("collections/{collectionId}/items/{itemId}/")
    suspend fun deleteCollectionItem(
        @Path("collectionId") collectionId: Int,
        @Path("itemId") itemId: Int
    ): Response<Unit>

    @GET("collections/{id}/value/")
    suspend fun getCollectionValue(@Path("id") id: Int): Response<CollectionValueDto>

    @GET("collections/{id}/value_history/")
    suspend fun getCollectionValueHistory(@Path("id") id: Int): Response<List<CollectionValueSnapshotDto>>

    @GET("collections/public/")
    suspend fun getPublicCollections(): Response<PaginatedResponse<CollectionDto>>
}
