package com.heroesandmore.app.data.dto.marketplace

import com.google.gson.annotations.SerializedName
import com.heroesandmore.app.data.dto.accounts.PublicProfileDto

data class ListingDto(
    val id: Int,
    val title: String,
    val price: String,
    @SerializedName("current_price")
    val currentPrice: String,
    @SerializedName("listing_type")
    val listingType: String,
    val condition: String,
    @SerializedName("seller_username")
    val sellerUsername: String,
    @SerializedName("category_name")
    val categoryName: String,
    @SerializedName("primary_image")
    val primaryImage: String?,
    @SerializedName("auction_end")
    val auctionEnd: String?,
    @SerializedName("time_remaining")
    val timeRemaining: Int?,
    val views: Int,
    val created: String
)

data class ListingDetailDto(
    val id: Int,
    val title: String,
    val description: String?,
    val price: String,
    @SerializedName("current_price")
    val currentPrice: String,
    @SerializedName("listing_type")
    val listingType: String,
    val condition: String,
    @SerializedName("grading_company")
    val gradingCompany: String?,
    val grade: String?,
    @SerializedName("cert_number")
    val certNumber: String?,
    val seller: PublicProfileDto,
    @SerializedName("category_name")
    val categoryName: String,
    @SerializedName("category_slug")
    val categorySlug: String,
    val images: List<ListingImageDto>,
    @SerializedName("allow_offers")
    val allowOffers: Boolean,
    @SerializedName("shipping_price")
    val shippingPrice: String?,
    @SerializedName("auction_end")
    val auctionEnd: String?,
    @SerializedName("bid_count")
    val bidCount: Int,
    @SerializedName("high_bidder")
    val highBidder: String?,
    @SerializedName("is_saved")
    val isSaved: Boolean,
    @SerializedName("recent_sales")
    val recentSales: List<RecentSaleDto>?,
    val views: Int,
    val status: String,
    val created: String
)

data class ListingImageDto(
    val id: Int,
    val image: String,
    val url: String,
    val order: Int
)

data class RecentSaleDto(
    val source: String,
    val price: String,
    val date: String
)

data class CreateListingRequest(
    val title: String,
    val description: String? = null,
    val price: String,
    @SerializedName("listing_type")
    val listingType: String = "fixed",
    val category: Int,
    val condition: String,
    @SerializedName("grading_company")
    val gradingCompany: String? = null,
    val grade: String? = null,
    @SerializedName("cert_number")
    val certNumber: String? = null,
    @SerializedName("allow_offers")
    val allowOffers: Boolean = true,
    @SerializedName("shipping_price")
    val shippingPrice: String? = null,
    @SerializedName("auction_end")
    val auctionEnd: String? = null
)

data class UpdateListingRequest(
    val title: String? = null,
    val description: String? = null,
    val price: String? = null,
    val condition: String? = null,
    @SerializedName("grading_company")
    val gradingCompany: String? = null,
    val grade: String? = null,
    @SerializedName("cert_number")
    val certNumber: String? = null,
    @SerializedName("allow_offers")
    val allowOffers: Boolean? = null,
    @SerializedName("shipping_price")
    val shippingPrice: String? = null
)

data class BidDto(
    val id: Int,
    val amount: String,
    @SerializedName("bidder_username")
    val bidderUsername: String,
    val created: String
)

data class BidRequest(
    val amount: String
)

data class OfferListingDto(
    val id: Int,
    val title: String,
    val price: String,
    @SerializedName("image_url")
    val imageUrl: String?
)

data class OfferDto(
    val id: Int,
    val listing: OfferListingDto,
    val amount: String,
    val message: String?,
    @SerializedName("buyer_username")
    val buyerUsername: String,
    val status: String,
    @SerializedName("is_from_buyer")
    val isFromBuyer: Boolean,
    @SerializedName("counter_amount")
    val counterAmount: String?,
    @SerializedName("counter_message")
    val counterMessage: String?,
    @SerializedName("time_remaining")
    val timeRemaining: String?,
    @SerializedName("expires_at")
    val expiresAt: String?,
    val created: String
)

data class OfferRequest(
    val amount: String,
    val message: String? = null
)

data class CounterOfferRequest(
    val amount: String
)

data class OrderDto(
    val id: Int,
    val listing: ListingDto,
    @SerializedName("buyer_username")
    val buyerUsername: String,
    @SerializedName("seller_username")
    val sellerUsername: String,
    @SerializedName("item_price")
    val itemPrice: String,
    @SerializedName("shipping_price")
    val shippingPrice: String,
    val amount: String,
    @SerializedName("platform_fee")
    val platformFee: String,
    @SerializedName("seller_payout")
    val sellerPayout: String,
    val status: String,
    @SerializedName("shipping_address")
    val shippingAddress: ShippingAddressDto?,
    @SerializedName("tracking_number")
    val trackingNumber: String?,
    @SerializedName("tracking_carrier")
    val trackingCarrier: String?,
    @SerializedName("shipped_at")
    val shippedAt: String?,
    @SerializedName("delivered_at")
    val deliveredAt: String?,
    val created: String
)

data class ShippingAddressDto(
    val name: String,
    @SerializedName("address_line1")
    val addressLine1: String,
    @SerializedName("address_line2")
    val addressLine2: String?,
    val city: String,
    val state: String,
    @SerializedName("postal_code")
    val postalCode: String,
    val country: String
)

data class ShipOrderRequest(
    @SerializedName("tracking_number")
    val trackingNumber: String,
    @SerializedName("tracking_carrier")
    val trackingCarrier: String
)

data class ReviewDto(
    val id: Int,
    val order: Int,
    val rating: Int,
    val text: String?,
    @SerializedName("reviewer_username")
    val reviewerUsername: String,
    val created: String
)

data class CreateReviewRequest(
    val rating: Int,
    val text: String? = null
)

data class CheckoutRequest(
    @SerializedName("shipping_address")
    val shippingAddress: ShippingAddressDto
)

data class PaymentIntentRequest(
    @SerializedName("listing_id")
    val listingId: Int,
    @SerializedName("offer_id")
    val offerId: Int? = null
)

data class PaymentIntentResponse(
    @SerializedName("client_secret")
    val clientSecret: String,
    @SerializedName("publishable_key")
    val publishableKey: String,
    val amount: Int
)

data class ConfirmPaymentRequest(
    @SerializedName("payment_intent_id")
    val paymentIntentId: String
)

data class AuctionEventDto(
    val id: Int,
    val title: String,
    val description: String?,
    @SerializedName("start_time")
    val startTime: String,
    @SerializedName("end_time")
    val endTime: String,
    @SerializedName("lot_count")
    val lotCount: Int,
    @SerializedName("is_live")
    val isLive: Boolean,
    val status: String,
    val created: String
)

data class AutoBidDto(
    val id: Int,
    val listing: Int,
    @SerializedName("listing_title")
    val listingTitle: String,
    @SerializedName("max_amount")
    val maxAmount: String,
    @SerializedName("current_bid")
    val currentBid: String?,
    @SerializedName("is_active")
    val isActive: Boolean,
    val created: String
)

data class AutoBidRequest(
    @SerializedName("max_amount")
    val maxAmount: String
)

data class SavedStatusResponse(
    @SerializedName("is_saved")
    val isSaved: Boolean
)

data class StatusResponse(
    val status: String,
    val message: String?
)

data class AcceptCounterResponse(
    val status: String,
    @SerializedName("order_id")
    val orderId: Int
)
