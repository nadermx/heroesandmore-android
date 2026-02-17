package com.heroesandmore.app.data.repository

import com.heroesandmore.app.data.api.SellerApi
import com.heroesandmore.app.data.dto.seller.*
import com.heroesandmore.app.domain.model.*
import com.heroesandmore.app.domain.repository.SellerRepository
import com.heroesandmore.app.util.Resource
import com.heroesandmore.app.util.safeApiCall
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SellerRepositoryImpl @Inject constructor(
    private val sellerApi: SellerApi
) : SellerRepository {

    override suspend fun getDashboard(): Resource<DashboardStats> {
        val result = safeApiCall { sellerApi.getDashboard() }
        return result.map { it.toDashboardStats() }
    }

    override suspend fun getAnalytics(days: Int): Resource<Analytics> {
        val result = safeApiCall { sellerApi.getAnalytics(days) }
        return result.map { it.toAnalytics() }
    }

    override suspend fun getSubscription(): Resource<Subscription> {
        val result = safeApiCall { sellerApi.getSubscription() }
        return result.map { it.toSubscription() }
    }

    override suspend fun getInventory(page: Int, search: String?, ordering: String?): Resource<List<InventoryItem>> {
        val result = safeApiCall { sellerApi.getInventory(page, search, ordering) }
        return result.map { response -> response.results.map { it.toInventoryItem() } }
    }

    override suspend fun addInventoryItem(request: CreateInventoryItemRequest): Resource<InventoryItem> {
        val result = safeApiCall { sellerApi.addInventoryItem(request) }
        return result.map { it.toInventoryItem() }
    }

    override suspend fun updateInventoryItem(id: Int, request: UpdateInventoryItemRequest): Resource<InventoryItem> {
        val result = safeApiCall { sellerApi.updateInventoryItem(id, request) }
        return result.map { it.toInventoryItem() }
    }

    override suspend fun deleteInventoryItem(id: Int): Resource<Boolean> {
        val result = safeApiCall { sellerApi.deleteInventoryItem(id) }
        return when (result) {
            is Resource.Success -> Resource.success(true)
            is Resource.Error -> Resource.error(result.message ?: "Failed to delete inventory item")
            is Resource.Loading -> Resource.loading()
        }
    }

    override suspend fun createListingFromInventory(id: Int): Resource<ListingDetail> {
        val result = safeApiCall { sellerApi.createListingFromInventory(id) }
        return result.map { it.toListingDetail() }
    }

    override suspend fun getBulkImports(page: Int): Resource<List<BulkImport>> {
        val result = safeApiCall { sellerApi.getBulkImports(page) }
        return result.map { response -> response.results.map { it.toBulkImport() } }
    }

    override suspend fun createBulkImport(file: File, autoPublish: Boolean, defaultCategory: Int?): Resource<BulkImport> {
        val requestBody = file.asRequestBody("text/csv".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("file", file.name, requestBody)
        val result = safeApiCall { sellerApi.createBulkImport(filePart, autoPublish, defaultCategory) }
        return result.map { it.toBulkImport() }
    }

    override suspend fun getBulkImport(id: Int): Resource<BulkImport> {
        val result = safeApiCall { sellerApi.getBulkImport(id) }
        return result.map { it.toBulkImport() }
    }

    override suspend fun getBulkImportRows(id: Int): Resource<List<BulkImportRow>> {
        val result = safeApiCall { sellerApi.getBulkImportRows(id) }
        return result.map { rows -> rows.map { it.toBulkImportRow() } }
    }

    override suspend fun getSellerOrders(page: Int): Resource<List<Order>> {
        val result = safeApiCall { sellerApi.getSellerOrders(page) }
        return result.map { response -> response.results.map { it.toOrder() } }
    }

    override suspend fun getSalesHistory(page: Int): Resource<List<Order>> {
        val result = safeApiCall { sellerApi.getSalesHistory(page) }
        return result.map { response -> response.results.map { it.toOrder() } }
    }

    // Mapping functions
    private fun DashboardStatsDto.toDashboardStats(): DashboardStats = DashboardStats(
        totalSales = totalSales,
        totalOrders = totalOrders,
        pendingOrders = pendingOrders,
        activeListings = activeListings,
        totalViews = totalViews,
        conversionRate = conversionRate,
        averageSalePrice = averageSalePrice,
        thisMonthSales = thisMonthSales,
        lastMonthSales = lastMonthSales,
        salesChangePercent = salesChangePercent
    )

    private fun AnalyticsDto.toAnalytics(): Analytics = Analytics(
        salesByDay = salesByDay.map { SalesByDay(it.date, it.sales, it.orders) },
        salesByCategory = salesByCategory.map { SalesByCategory(it.category, it.sales, it.count, it.percentage) },
        topItems = topItems.map { TopItem(it.id, it.title, it.image, it.sales, it.quantity) },
        trafficSources = trafficSources?.map { TrafficSource(it.source, it.visits, it.conversions, it.conversionRate) }
    )

    private fun SubscriptionDto.toSubscription(): Subscription = Subscription(
        id = id,
        plan = plan,
        planName = planName,
        status = SubscriptionStatus.fromString(status),
        currentPeriodStart = currentPeriodStart,
        currentPeriodEnd = currentPeriodEnd,
        cancelAtPeriodEnd = cancelAtPeriodEnd,
        features = features
    )

    private fun InventoryItemDto.toInventoryItem(): InventoryItem = InventoryItem(
        id = id,
        name = name,
        description = description,
        image = image,
        categoryId = categoryId,
        categoryName = categoryName,
        quantity = quantity,
        costPrice = costPrice,
        targetPrice = targetPrice,
        condition = condition,
        grade = grade,
        gradingCompany = gradingCompany,
        location = location,
        sku = sku,
        isListed = isListed,
        listingId = listingId,
        created = created
    )

    private fun BulkImportDto.toBulkImport(): BulkImport = BulkImport(
        id = id,
        filename = filename,
        status = ImportStatus.fromString(status),
        totalRows = totalRows,
        processedRows = processedRows,
        successCount = successCount,
        errorCount = errorCount,
        autoPublish = autoPublish,
        defaultCategory = defaultCategory,
        created = created,
        completed = completed
    )

    private fun BulkImportRowDto.toBulkImportRow(): BulkImportRow = BulkImportRow(
        id = id,
        rowNumber = rowNumber,
        status = status,
        rawData = rawData,
        listingId = listingId,
        errorMessage = errorMessage
    )

    private fun com.heroesandmore.app.data.dto.marketplace.OrderDto.toOrder(): Order = Order(
        id = id,
        listing = listing.toListing(),
        buyerUsername = buyerUsername,
        sellerUsername = sellerUsername,
        itemPrice = itemPrice,
        shippingPrice = shippingPrice,
        amount = amount,
        platformFee = platformFee,
        sellerPayout = sellerPayout,
        status = OrderStatus.fromString(status),
        shippingAddress = shippingAddress?.let {
            ShippingAddress(it.name, it.addressLine1, it.addressLine2, it.city, it.state, it.postalCode, it.country)
        },
        trackingNumber = trackingNumber,
        trackingCarrier = trackingCarrier,
        shippedAt = shippedAt,
        deliveredAt = deliveredAt,
        created = created
    )

    private fun com.heroesandmore.app.data.dto.marketplace.ListingDto.toListing(): Listing = Listing(
        id = id,
        title = title,
        price = price,
        currentPrice = currentPrice,
        listingType = ListingType.fromString(listingType),
        condition = condition,
        sellerUsername = sellerUsername,
        categoryName = categoryName,
        primaryImage = primaryImage,
        auctionEnd = auctionEnd,
        timeRemaining = timeRemaining,
        views = views,
        created = created,
        sellerIsTrusted = sellerIsTrusted,
        saveCount = saveCount,
        recentBids = recentBids
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
        created = created,
        watcherCount = watcherCount,
        recentBidCount = recentBidCount,
        bidWarActive = bidWarActive,
        compsRange = compsRange?.let { CompsRange(it.low, it.high) },
        bidHistory = bidHistory.map { BidHistoryItem(it.bidder, it.amount, it.created) },
        sellerIsTrusted = sellerIsTrusted
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
}
