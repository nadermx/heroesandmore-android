package com.heroesandmore.app.domain.repository

import com.heroesandmore.app.data.dto.alerts.*
import com.heroesandmore.app.domain.model.*
import com.heroesandmore.app.util.Resource

interface AlertsRepository {
    // Notifications
    suspend fun getNotifications(page: Int = 1): Resource<List<Alert>>
    suspend fun markAsRead(alertId: Int): Resource<Boolean>
    suspend fun markAllAsRead(): Resource<Boolean>

    // Wishlists
    suspend fun getWishlists(): Resource<List<Wishlist>>
    suspend fun createWishlist(name: String, notifyOnMatch: Boolean): Resource<Wishlist>
    suspend fun getWishlistDetail(id: Int): Resource<WishlistDetail>
    suspend fun updateWishlist(id: Int, name: String?, notifyOnMatch: Boolean?): Resource<Wishlist>
    suspend fun deleteWishlist(id: Int): Resource<Boolean>
    suspend fun addWishlistItem(wishlistId: Int, request: CreateWishlistItemRequest): Resource<WishlistItem>
    suspend fun removeWishlistItem(wishlistId: Int, itemId: Int): Resource<Boolean>

    // Saved Searches
    suspend fun getSavedSearches(): Resource<List<SavedSearch>>
    suspend fun createSavedSearch(request: CreateSavedSearchRequest): Resource<SavedSearch>
    suspend fun deleteSavedSearch(id: Int): Resource<Boolean>
    suspend fun getSavedSearchMatches(id: Int): Resource<List<Listing>>

    // Price Alerts
    suspend fun getPriceAlerts(): Resource<List<PriceAlert>>
    suspend fun createPriceAlert(priceGuideItem: Int, targetPrice: String, alertType: String): Resource<PriceAlert>
    suspend fun updatePriceAlert(id: Int, targetPrice: String?, alertType: String?): Resource<PriceAlert>
    suspend fun deletePriceAlert(id: Int): Resource<Boolean>
}
