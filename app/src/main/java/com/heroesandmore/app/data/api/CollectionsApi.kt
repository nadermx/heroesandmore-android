package com.heroesandmore.app.data.api

import com.heroesandmore.app.data.dto.collections.*
import com.heroesandmore.app.data.dto.common.PaginatedResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface CollectionsApi {

    @GET("collections/mine/")
    suspend fun getCollections(): Response<List<CollectionDto>>

    @GET("collections/{id}/")
    suspend fun getCollection(@Path("id") id: Int): Response<CollectionDto>

    @GET("collections/{id}/detail/")
    suspend fun getCollectionDetail(@Path("id") id: Int): Response<CollectionDetailDto>

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
    suspend fun removeCollectionItem(
        @Path("collectionId") collectionId: Int,
        @Path("itemId") itemId: Int
    ): Response<Unit>

    @GET("collections/{id}/value/")
    suspend fun getCollectionValue(@Path("id") id: Int): Response<CollectionValueDto>

    @GET("collections/{id}/value_history/")
    suspend fun getCollectionValueHistory(@Path("id") id: Int): Response<List<CollectionValueSnapshotDto>>

    @GET("collections/{id}/export/")
    suspend fun exportCollection(
        @Path("id") id: Int,
        @Query("export_format") format: String = "json"
    ): Response<ResponseBody>

    @Multipart
    @POST("collections/import/")
    suspend fun importCollection(@Part file: MultipartBody.Part): Response<ImportResultDto>

    @GET("collections/public/")
    suspend fun getPublicCollections(
        @Query("page") page: Int = 1
    ): Response<PaginatedResponse<CollectionDto>>
}
