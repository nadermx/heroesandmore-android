package com.heroesandmore.app.data.api

import com.heroesandmore.app.data.dto.common.PaginatedResponse
import com.heroesandmore.app.data.dto.marketplace.ListingDetailDto
import com.heroesandmore.app.data.dto.scanner.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ScannerApi {

    @Multipart
    @POST("scanner/scan/")
    suspend fun uploadScan(@Part image: MultipartBody.Part): Response<ScanResultDto>

    @GET("scanner/scan/{id}/")
    suspend fun getScanResult(@Path("id") scanId: Int): Response<ScanResultDto>

    @GET("scanner/scans/")
    suspend fun getScanHistory(
        @Query("page") page: Int = 1
    ): Response<PaginatedResponse<ScanResultDto>>

    @POST("scanner/scan/{id}/create-listing/")
    suspend fun createListingFromScan(
        @Path("id") scanId: Int,
        @Body request: CreateFromScanRequest
    ): Response<ListingDetailDto>

    @POST("scanner/scan/{id}/add-to-collection/")
    suspend fun addToCollectionFromScan(
        @Path("id") scanId: Int,
        @Body request: AddToCollectionFromScanRequest
    ): Response<com.heroesandmore.app.data.dto.collections.CollectionItemDto>

    // Scan Sessions
    @GET("scanner/sessions/")
    suspend fun getScanSessions(
        @Query("page") page: Int = 1
    ): Response<PaginatedResponse<ScanSessionDto>>

    @POST("scanner/sessions/")
    suspend fun createScanSession(@Body request: CreateScanSessionRequest): Response<ScanSessionDto>

    @Multipart
    @POST("scanner/sessions/{id}/scan/")
    suspend fun addScanToSession(
        @Path("id") sessionId: Int,
        @Part image: MultipartBody.Part
    ): Response<ScanResultDto>
}
