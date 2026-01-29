package com.heroesandmore.app.domain.repository

import com.heroesandmore.app.data.dto.scanner.*
import com.heroesandmore.app.domain.model.*
import com.heroesandmore.app.util.Resource
import java.io.File

interface ScannerRepository {
    suspend fun uploadScan(imageFile: File): Resource<ScanResult>
    suspend fun getScanResult(scanId: Int): Resource<ScanResult>
    suspend fun getScanHistory(page: Int = 1): Resource<List<ScanResult>>
    suspend fun createListingFromScan(scanId: Int, request: CreateFromScanRequest): Resource<ListingDetail>
    suspend fun addToCollectionFromScan(scanId: Int, request: AddToCollectionFromScanRequest): Resource<CollectionItem>

    // Scan Sessions
    suspend fun getScanSessions(page: Int = 1): Resource<List<ScanSession>>
    suspend fun createScanSession(name: String?, destinationType: String?, destinationId: Int?): Resource<ScanSession>
    suspend fun addScanToSession(sessionId: Int, imageFile: File): Resource<ScanResult>
}
