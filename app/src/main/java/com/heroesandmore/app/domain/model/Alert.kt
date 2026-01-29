package com.heroesandmore.app.domain.model

data class Alert(
    val id: Int,
    val type: AlertType,
    val title: String,
    val message: String,
    val itemId: Int?,
    val listingId: Int?,
    val read: Boolean,
    val created: String
)

enum class AlertType {
    BID,
    OUTBID,
    AUCTION_WON,
    AUCTION_ENDED,
    OFFER_RECEIVED,
    OFFER_ACCEPTED,
    OFFER_DECLINED,
    ORDER_SHIPPED,
    ORDER_DELIVERED,
    PRICE_ALERT,
    WISHLIST_MATCH,
    NEW_FOLLOWER,
    NEW_MESSAGE,
    SYSTEM;

    companion object {
        fun fromString(value: String): AlertType {
            return when (value.lowercase()) {
                "bid" -> BID
                "outbid" -> OUTBID
                "auction_won" -> AUCTION_WON
                "auction_ended" -> AUCTION_ENDED
                "offer_received" -> OFFER_RECEIVED
                "offer_accepted" -> OFFER_ACCEPTED
                "offer_declined" -> OFFER_DECLINED
                "order_shipped" -> ORDER_SHIPPED
                "order_delivered" -> ORDER_DELIVERED
                "price_alert" -> PRICE_ALERT
                "wishlist_match" -> WISHLIST_MATCH
                "new_follower" -> NEW_FOLLOWER
                "new_message" -> NEW_MESSAGE
                else -> SYSTEM
            }
        }
    }
}

data class Wishlist(
    val id: Int,
    val name: String,
    val itemCount: Int,
    val notifyOnMatch: Boolean,
    val created: String
)

data class WishlistDetail(
    val id: Int,
    val name: String,
    val notifyOnMatch: Boolean,
    val items: List<WishlistItem>,
    val created: String
)

data class WishlistItem(
    val id: Int,
    val searchQuery: String?,
    val category: Int?,
    val categoryName: String?,
    val maxPrice: String?,
    val minCondition: String?,
    val matchCount: Int?,
    val created: String
)

data class SavedSearch(
    val id: Int,
    val name: String,
    val query: String,
    val category: Int?,
    val categoryName: String?,
    val minPrice: String?,
    val maxPrice: String?,
    val condition: String?,
    val notifyOnMatch: Boolean,
    val matchCount: Int?,
    val created: String
)

data class PriceAlert(
    val id: Int,
    val priceGuideItem: Int,
    val itemName: String,
    val itemImage: String?,
    val targetPrice: String,
    val currentPrice: String?,
    val alertType: PriceAlertType,
    val isTriggered: Boolean,
    val triggeredAt: String?,
    val created: String
)

enum class PriceAlertType {
    BELOW,
    ABOVE;

    companion object {
        fun fromString(value: String): PriceAlertType {
            return when (value.lowercase()) {
                "above" -> ABOVE
                else -> BELOW
            }
        }
    }
}
