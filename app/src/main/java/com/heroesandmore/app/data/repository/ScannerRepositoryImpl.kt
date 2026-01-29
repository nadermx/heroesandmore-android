package com.heroesandmore.app.data.repository

import com.heroesandmore.app.data.api.ScannerApi
import com.heroesandmore.app.data.dto.scanner.*
import com.heroesandmore.app.domain.model.*
import com.heroesandmore.app.domain.repository.ScannerRepository
import com.heroesandmore.app.util.Resource
import com.heroesandmore.app.util.safeApiCall
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScannerRepositoryImpl @Inject constructor(
    private val scannerApi: ScannerApi
) : ScannerRepository {

    override suspend fun uploadScan(imageFile: File): Resource<ScanResult> {
        val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("image", imageFile.name, requestBody)
        val result = safeApiCall { scannerApi.uploadScan(part) }
        return result.map { it.toScanResult() }
    }

    override suspend fun getScanResult(scanId: Int): Resource<ScanResult> {
        val result = safeApiCall { scannerApi.getScanResult(scanId) }
        return result.map { it.toScanResult() }
    }

    override suspend fun getScanHistory(page: Int): Resource<List<ScanResult>> {
        val result = safeApiCall { scannerApi.getScanHistory(page) }
        return result.map { response -> response.results.map { it.toScanResult() } }
    }

    override suspend fun createListingFromScan(scanId: Int, request: CreateFromScanRequest): Resource<ListingDetail> {
        val result = safeApiCall { scannerApi.createListingFromScan(scanId, request) }
        return result.map { it.toListingDetail() }
    }

    override suspend fun addToCollectionFromScan(scanId: Int, request: AddToCollectionFromScanRequest): Resource<CollectionItem> {
        val result = safeApiCall { scannerApi.addToCollectionFromScan(scanId, request) }
        return result.map { it.toCollectionItem() }
    }

    override suspend fun getScanSessions(page: Int): Resource<List<ScanSession>> {
        val result = safeApiCall { scannerApi.getScanSessions(page) }
        return result.map { response -> response.results.map { it.toScanSession() } }
    }

    override suspend fun createScanSession(name: String?, destinationType: String?, destinationId: Int?): Resource<ScanSession> {
        val result = safeApiCall { scannerApi.createScanSession(CreateScanSessionRequest(name, destinationType, destinationId)) }
        return result.map { it.toScanSession() }
    }

    override suspend fun addScanToSession(sessionId: Int, imageFile: File): Resource<ScanResult> {
        val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("image", imageFile.name, requestBody)
        val result = safeApiCall { scannerApi.addScanToSession(sessionId, part) }
        return result.map { it.toScanResult() }
    }

    // Mapping functions
    private fun ScanResultDto.toScanResult(): ScanResult = ScanResult(
        id = id,
        status = ScanStatus.fromString(status),
        scannedImage = scannedImage,
        identifiedItem = identifiedItem?.toIdentifiedItem(),
        confidenceScore = confidenceScore,
        alternativeMatches = alternativeMatches?.map { it.toAlternativeMatch() } ?: emptyList(),
        processingTime = processingTime,
        created = created
    )

    private fun IdentifiedItemDto.toIdentifiedItem(): IdentifiedItem = IdentifiedItem(
        id = id,
        name = name,
        category = category,
        categoryId = categoryId,
        year = year,
        set = set,
        cardNumber = cardNumber,
        estimatedValue = estimatedValue,
        priceRangeLow = priceRangeLow,
        priceRangeHigh = priceRangeHigh,
        imageUrl = imageUrl,
        priceGuideId = priceGuideId
    )

    private fun AlternativeMatchDto.toAlternativeMatch(): AlternativeMatch = AlternativeMatch(
        id = id,
        name = name,
        confidenceScore = confidenceScore,
        estimatedValue = estimatedValue,
        imageUrl = imageUrl
    )

    private fun ScanSessionDto.toScanSession(): ScanSession = ScanSession(
        id = id,
        name = name,
        scanCount = scanCount,
        status = status,
        destinationType = destinationType,
        destinationId = destinationId,
        created = created
    )

    private fun com.heroesandmore.app.data.dto.marketplace.ListingDetailDto.toListingDetail(): ListingDetail = ListingDetail(
        id = id,
        title = title,
        description = description,
        price = price,
        currentPrice = currentPrice,
        listingType = ListingType.fromString(listingType),
        condition = condition,
        gradingCompany = gradingCompany,
        grade = grade,
        certNumber = certNumber,
        seller = seller.toPublicProfile(),
        categoryName = categoryName,
        categorySlug = categorySlug,
        images = images.map { ListingImage(it.id, it.url, it.order) },
        allowOffers = allowOffers,
        shippingPrice = shippingPrice,
        auctionEnd = auctionEnd,
        bidCount = bidCount,
        highBidder = highBidder,
        isSaved = isSaved,
        recentSales = recentSales?.map { RecentSale(it.source, it.price, it.date) } ?: emptyList(),
        views = views,
        status = status,
        created = created
    )

    private fun com.heroesandmore.app.data.dto.accounts.PublicProfileDto.toPublicProfile(): PublicProfile = PublicProfile(
        username = username,
        avatarUrl = avatarUrl,
        bio = bio,
        location = location,
        rating = rating,
        isSellerVerified = isSellerVerified,
        listingsCount = listingsCount,
        created = created
    )

    private fun com.heroesandmore.app.data.dto.collections.CollectionItemDto.toCollectionItem(): CollectionItem = CollectionItem(
        id = id,
        itemName = itemName,
        condition = condition,
        grade = grade,
        gradingCompany = gradingCompany,
        purchasePrice = purchasePrice,
        purchaseDate = purchaseDate,
        currentValue = currentValue,
        imageUrl = imageUrl,
        notes = notes,
        created = created
    )
}
