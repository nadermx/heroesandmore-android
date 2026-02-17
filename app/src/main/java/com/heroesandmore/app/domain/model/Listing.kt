package com.heroesandmore.app.domain.model

data class Listing(
    val id: Int,
    val title: String,
    val price: String,
    val currentPrice: String,
    val listingType: ListingType,
    val condition: String,
    val sellerUsername: String,
    val categoryName: String,
    val primaryImage: String?,
    val auctionEnd: String?,
    val timeRemaining: Int?,
    val views: Int,
    val created: String,
    val quantityAvailable: Int = 1,
    val isPlatformListing: Boolean = false,
    val sellerIsTrusted: Boolean = false
)

data class ListingDetail(
    val id: Int,
    val title: String,
    val description: String?,
    val price: String,
    val currentPrice: String,
    val listingType: ListingType,
    val condition: String,
    val gradingCompany: String?,
    val grade: String?,
    val certNumber: String?,
    val seller: PublicProfile,
    val categoryName: String,
    val categorySlug: String,
    val images: List<ListingImage>,
    val allowOffers: Boolean,
    val shippingPrice: String?,
    val auctionEnd: String?,
    val bidCount: Int,
    val highBidder: String?,
    val isSaved: Boolean,
    val recentSales: List<RecentSale>,
    val views: Int,
    val status: String,
    val created: String,
    val quantity: Int = 1,
    val quantityAvailable: Int = 1,
    val quantitySold: Int = 0,
    val isPlatformListing: Boolean = false
)

data class ListingImage(
    val id: Int,
    val url: String,
    val order: Int
)

data class RecentSale(
    val source: String,
    val price: String,
    val date: String
)

enum class ListingType {
    FIXED,
    AUCTION;

    companion object {
        fun fromString(value: String): ListingType {
            return when (value.lowercase()) {
                "auction" -> AUCTION
                else -> FIXED
            }
        }
    }
}

data class Bid(
    val id: Int,
    val amount: String,
    val bidderUsername: String,
    val created: String
)

data class OfferListing(
    val id: Int,
    val title: String,
    val price: String,
    val imageUrl: String?
)

data class Offer(
    val id: Int,
    val listing: OfferListing,
    val amount: String,
    val message: String?,
    val buyerUsername: String,
    val status: OfferStatus,
    val isFromBuyer: Boolean,
    val counterAmount: String?,
    val counterMessage: String?,
    val timeRemaining: String?,
    val created: String
)

enum class OfferStatus {
    PENDING,
    ACCEPTED,
    DECLINED,
    COUNTERED,
    EXPIRED;

    companion object {
        fun fromString(value: String): OfferStatus {
            return when (value.lowercase()) {
                "accepted" -> ACCEPTED
                "declined" -> DECLINED
                "countered" -> COUNTERED
                "expired" -> EXPIRED
                else -> PENDING
            }
        }
    }
}

data class Order(
    val id: Int,
    val listing: Listing,
    val buyerUsername: String,
    val sellerUsername: String,
    val quantity: Int = 1,
    val itemPrice: String,
    val shippingPrice: String,
    val amount: String,
    val platformFee: String,
    val sellerPayout: String,
    val status: OrderStatus,
    val shippingAddress: ShippingAddress?,
    val trackingNumber: String?,
    val trackingCarrier: String?,
    val shippedAt: String?,
    val deliveredAt: String?,
    val created: String
)

enum class OrderStatus {
    PENDING,
    PAID,
    SHIPPED,
    DELIVERED,
    COMPLETED,
    CANCELLED,
    REFUNDED;

    companion object {
        fun fromString(value: String): OrderStatus {
            return when (value.lowercase()) {
                "paid" -> PAID
                "shipped" -> SHIPPED
                "delivered" -> DELIVERED
                "completed" -> COMPLETED
                "cancelled" -> CANCELLED
                "refunded" -> REFUNDED
                else -> PENDING
            }
        }
    }
}

data class ShippingAddress(
    val name: String,
    val addressLine1: String,
    val addressLine2: String?,
    val city: String,
    val state: String,
    val postalCode: String,
    val country: String
)

data class Review(
    val id: Int,
    val orderId: Int,
    val rating: Int,
    val text: String?,
    val reviewerUsername: String,
    val created: String
)
