package com.heroesandmore.app.data.dto.alerts

import com.google.gson.annotations.SerializedName

data class AlertDto(
    val id: Int,
    val type: String,
    val title: String,
    val message: String,
    @SerializedName("item_id")
    val itemId: Int?,
    @SerializedName("listing_id")
    val listingId: Int?,
    val read: Boolean,
    val created: String
)

data class WishlistDto(
    val id: Int,
    val name: String,
    @SerializedName("item_count")
    val itemCount: Int,
    @SerializedName("notify_on_match")
    val notifyOnMatch: Boolean,
    val created: String
)

data class WishlistDetailDto(
    val id: Int,
    val name: String,
    @SerializedName("notify_on_match")
    val notifyOnMatch: Boolean,
    val items: List<WishlistItemDto>,
    val created: String
)

data class WishlistItemDto(
    val id: Int,
    @SerializedName("search_query")
    val searchQuery: String?,
    val category: Int?,
    @SerializedName("category_name")
    val categoryName: String?,
    @SerializedName("max_price")
    val maxPrice: String?,
    @SerializedName("min_condition")
    val minCondition: String?,
    @SerializedName("match_count")
    val matchCount: Int?,
    val created: String
)

data class CreateWishlistRequest(
    val name: String,
    @SerializedName("notify_on_match")
    val notifyOnMatch: Boolean = true
)

data class UpdateWishlistRequest(
    val name: String? = null,
    @SerializedName("notify_on_match")
    val notifyOnMatch: Boolean? = null
)

data class CreateWishlistItemRequest(
    @SerializedName("search_query")
    val searchQuery: String? = null,
    val category: Int? = null,
    @SerializedName("max_price")
    val maxPrice: String? = null,
    @SerializedName("min_condition")
    val minCondition: String? = null
)

data class SavedSearchDto(
    val id: Int,
    val name: String,
    val query: String,
    val category: Int?,
    @SerializedName("category_name")
    val categoryName: String?,
    @SerializedName("min_price")
    val minPrice: String?,
    @SerializedName("max_price")
    val maxPrice: String?,
    val condition: String?,
    @SerializedName("notify_on_match")
    val notifyOnMatch: Boolean,
    @SerializedName("match_count")
    val matchCount: Int?,
    val created: String
)

data class CreateSavedSearchRequest(
    val name: String,
    val query: String,
    val category: Int? = null,
    @SerializedName("min_price")
    val minPrice: String? = null,
    @SerializedName("max_price")
    val maxPrice: String? = null,
    val condition: String? = null,
    @SerializedName("notify_on_match")
    val notifyOnMatch: Boolean = true
)

data class PriceAlertDto(
    val id: Int,
    @SerializedName("price_guide_item")
    val priceGuideItem: Int,
    @SerializedName("item_name")
    val itemName: String,
    @SerializedName("item_image")
    val itemImage: String?,
    @SerializedName("target_price")
    val targetPrice: String,
    @SerializedName("current_price")
    val currentPrice: String?,
    @SerializedName("alert_type")
    val alertType: String,
    @SerializedName("is_triggered")
    val isTriggered: Boolean,
    @SerializedName("triggered_at")
    val triggeredAt: String?,
    val created: String
)

data class CreatePriceAlertRequest(
    @SerializedName("price_guide_item")
    val priceGuideItem: Int,
    @SerializedName("target_price")
    val targetPrice: String,
    @SerializedName("alert_type")
    val alertType: String = "below"
)

data class UpdatePriceAlertRequest(
    @SerializedName("target_price")
    val targetPrice: String? = null,
    @SerializedName("alert_type")
    val alertType: String? = null
)
