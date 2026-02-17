package com.heroesandmore.app.data.repository

import com.heroesandmore.app.data.api.AlertsApi
import com.heroesandmore.app.data.dto.alerts.*
import com.heroesandmore.app.domain.model.*
import com.heroesandmore.app.domain.repository.AlertsRepository
import com.heroesandmore.app.util.Resource
import com.heroesandmore.app.util.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlertsRepositoryImpl @Inject constructor(
    private val alertsApi: AlertsApi
) : AlertsRepository {

    override suspend fun getNotifications(page: Int): Resource<List<Alert>> {
        val result = safeApiCall { alertsApi.getNotifications(page) }
        return result.map { response -> response.results.map { it.toAlert() } }
    }

    override suspend fun markAsRead(alertId: Int): Resource<Boolean> {
        val result = safeApiCall { alertsApi.markAsRead(alertId) }
        return when (result) {
            is Resource.Success -> Resource.success(true)
            is Resource.Error -> Resource.error(result.message ?: "Failed to mark as read")
            is Resource.Loading -> Resource.loading()
            else -> Resource.error("Unknown error")
        }
    }

    override suspend fun markAllAsRead(): Resource<Boolean> {
        val result = safeApiCall { alertsApi.markAllAsRead() }
        return when (result) {
            is Resource.Success -> Resource.success(true)
            is Resource.Error -> Resource.error(result.message ?: "Failed to mark all as read")
            is Resource.Loading -> Resource.loading()
            else -> Resource.error("Unknown error")
        }
    }

    override suspend fun getWishlists(): Resource<List<Wishlist>> {
        val result = safeApiCall { alertsApi.getWishlists() }
        return result.map { wishlists -> wishlists.map { it.toWishlist() } }
    }

    override suspend fun createWishlist(name: String, notifyOnMatch: Boolean): Resource<Wishlist> {
        val result = safeApiCall { alertsApi.createWishlist(CreateWishlistRequest(name, notifyOnMatch)) }
        return result.map { it.toWishlist() }
    }

    override suspend fun getWishlistDetail(id: Int): Resource<WishlistDetail> {
        val result = safeApiCall { alertsApi.getWishlist(id) }
        return result.map { it.toWishlistDetail() }
    }

    override suspend fun updateWishlist(id: Int, name: String?, notifyOnMatch: Boolean?): Resource<Wishlist> {
        val result = safeApiCall { alertsApi.updateWishlist(id, UpdateWishlistRequest(name, notifyOnMatch)) }
        return result.map { it.toWishlist() }
    }

    override suspend fun deleteWishlist(id: Int): Resource<Boolean> {
        val result = safeApiCall { alertsApi.deleteWishlist(id) }
        return when (result) {
            is Resource.Success -> Resource.success(true)
            is Resource.Error -> Resource.error(result.message ?: "Failed to delete wishlist")
            is Resource.Loading -> Resource.loading()
            else -> Resource.error("Unknown error")
        }
    }

    override suspend fun addWishlistItem(wishlistId: Int, request: CreateWishlistItemRequest): Resource<WishlistItem> {
        val result = safeApiCall { alertsApi.addWishlistItem(wishlistId, request) }
        return result.map { it.toWishlistItem() }
    }

    override suspend fun removeWishlistItem(wishlistId: Int, itemId: Int): Resource<Boolean> {
        val result = safeApiCall { alertsApi.deleteWishlistItem(wishlistId, itemId) }
        return when (result) {
            is Resource.Success -> Resource.success(true)
            is Resource.Error -> Resource.error(result.message ?: "Failed to remove item")
            is Resource.Loading -> Resource.loading()
            else -> Resource.error("Unknown error")
        }
    }

    override suspend fun getSavedSearches(): Resource<List<SavedSearch>> {
        val result = safeApiCall { alertsApi.getSavedSearches() }
        return result.map { searches -> searches.map { it.toSavedSearch() } }
    }

    override suspend fun createSavedSearch(request: CreateSavedSearchRequest): Resource<SavedSearch> {
        val result = safeApiCall { alertsApi.createSavedSearch(request) }
        return result.map { it.toSavedSearch() }
    }

    override suspend fun deleteSavedSearch(id: Int): Resource<Boolean> {
        val result = safeApiCall { alertsApi.deleteSavedSearch(id) }
        return when (result) {
            is Resource.Success -> Resource.success(true)
            is Resource.Error -> Resource.error(result.message ?: "Failed to delete saved search")
            is Resource.Loading -> Resource.loading()
            else -> Resource.error("Unknown error")
        }
    }

    override suspend fun getSavedSearchMatches(id: Int): Resource<List<Listing>> {
        val result = safeApiCall { alertsApi.getSavedSearchMatches(id) }
        return result.map { listings -> listings.map { it.toListing() } }
    }

    override suspend fun getPriceAlerts(): Resource<List<PriceAlert>> {
        val result = safeApiCall { alertsApi.getPriceAlerts() }
        return result.map { alerts -> alerts.map { it.toPriceAlert() } }
    }

    override suspend fun createPriceAlert(priceGuideItem: Int, targetPrice: String, alertType: String): Resource<PriceAlert> {
        val result = safeApiCall { alertsApi.createPriceAlert(CreatePriceAlertRequest(priceGuideItem, targetPrice, alertType)) }
        return result.map { it.toPriceAlert() }
    }

    override suspend fun updatePriceAlert(id: Int, targetPrice: String?, alertType: String?): Resource<PriceAlert> {
        val result = safeApiCall { alertsApi.updatePriceAlert(id, UpdatePriceAlertRequest(targetPrice, alertType)) }
        return result.map { it.toPriceAlert() }
    }

    override suspend fun deletePriceAlert(id: Int): Resource<Boolean> {
        val result = safeApiCall { alertsApi.deletePriceAlert(id) }
        return when (result) {
            is Resource.Success -> Resource.success(true)
            is Resource.Error -> Resource.error(result.message ?: "Failed to delete price alert")
            is Resource.Loading -> Resource.loading()
            else -> Resource.error("Unknown error")
        }
    }

    // Mapping functions
    private fun AlertDto.toAlert(): Alert = Alert(
        id = id,
        type = AlertType.fromString(type),
        title = title,
        message = message,
        itemId = itemId,
        listingId = listingId,
        read = read,
        created = created
    )

    private fun WishlistDto.toWishlist(): Wishlist = Wishlist(
        id = id,
        name = name,
        itemCount = itemCount,
        notifyOnMatch = notifyOnMatch,
        created = created
    )

    private fun WishlistDetailDto.toWishlistDetail(): WishlistDetail = WishlistDetail(
        id = id,
        name = name,
        notifyOnMatch = notifyOnMatch,
        items = items.map { it.toWishlistItem() },
        created = created
    )

    private fun WishlistItemDto.toWishlistItem(): WishlistItem = WishlistItem(
        id = id,
        searchQuery = searchQuery,
        category = category,
        categoryName = categoryName,
        maxPrice = maxPrice,
        minCondition = minCondition,
        matchCount = matchCount,
        created = created
    )

    private fun SavedSearchDto.toSavedSearch(): SavedSearch = SavedSearch(
        id = id,
        name = name,
        query = query,
        category = category,
        categoryName = categoryName,
        minPrice = minPrice,
        maxPrice = maxPrice,
        condition = condition,
        notifyOnMatch = notifyOnMatch,
        matchCount = matchCount,
        created = created
    )

    private fun PriceAlertDto.toPriceAlert(): PriceAlert = PriceAlert(
        id = id,
        priceGuideItem = priceGuideItem,
        itemName = itemName,
        itemImage = itemImage,
        targetPrice = targetPrice,
        currentPrice = currentPrice,
        alertType = PriceAlertType.fromString(alertType),
        isTriggered = isTriggered,
        triggeredAt = triggeredAt,
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
        sellerIsTrusted = sellerIsTrusted
    )
}
