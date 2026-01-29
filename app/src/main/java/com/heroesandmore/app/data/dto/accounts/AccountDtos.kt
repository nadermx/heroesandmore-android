package com.heroesandmore.app.data.dto.accounts

import com.google.gson.annotations.SerializedName

data class ProfileDto(
    val id: Int,
    val username: String,
    val email: String?,
    val avatar: String?,
    @SerializedName("avatar_url")
    val avatarUrl: String?,
    val bio: String?,
    val location: String?,
    val website: String?,
    @SerializedName("is_seller_verified")
    val isSellerVerified: Boolean,
    val rating: Double?,
    @SerializedName("stripe_account_complete")
    val stripeAccountComplete: Boolean?,
    val created: String
)

data class PublicProfileDto(
    val username: String,
    val avatar: String?,
    @SerializedName("avatar_url")
    val avatarUrl: String?,
    val bio: String?,
    val location: String?,
    val rating: Double?,
    @SerializedName("is_seller_verified")
    val isSellerVerified: Boolean,
    @SerializedName("listings_count")
    val listingsCount: Int,
    val created: String
)

data class UpdateProfileRequest(
    val bio: String? = null,
    val location: String? = null,
    val website: String? = null
)

data class NotificationSettingsDto(
    @SerializedName("email_new_message")
    val emailNewMessage: Boolean,
    @SerializedName("email_new_offer")
    val emailNewOffer: Boolean,
    @SerializedName("email_outbid")
    val emailOutbid: Boolean,
    @SerializedName("email_auction_won")
    val emailAuctionWon: Boolean,
    @SerializedName("email_order_shipped")
    val emailOrderShipped: Boolean,
    @SerializedName("email_price_alert")
    val emailPriceAlert: Boolean,
    @SerializedName("email_wishlist_match")
    val emailWishlistMatch: Boolean,
    @SerializedName("push_enabled")
    val pushEnabled: Boolean
)

data class UpdateNotificationSettingsRequest(
    @SerializedName("email_new_message")
    val emailNewMessage: Boolean? = null,
    @SerializedName("email_new_offer")
    val emailNewOffer: Boolean? = null,
    @SerializedName("email_outbid")
    val emailOutbid: Boolean? = null,
    @SerializedName("email_auction_won")
    val emailAuctionWon: Boolean? = null,
    @SerializedName("email_order_shipped")
    val emailOrderShipped: Boolean? = null,
    @SerializedName("email_price_alert")
    val emailPriceAlert: Boolean? = null,
    @SerializedName("email_wishlist_match")
    val emailWishlistMatch: Boolean? = null,
    @SerializedName("push_enabled")
    val pushEnabled: Boolean? = null
)

data class DeviceTokenRequest(
    val token: String,
    val platform: String = "android"
)

data class RecentlyViewedDto(
    val id: Int,
    @SerializedName("listing_id")
    val listingId: Int,
    @SerializedName("listing_title")
    val listingTitle: String,
    @SerializedName("listing_image")
    val listingImage: String?,
    @SerializedName("listing_price")
    val listingPrice: String,
    @SerializedName("viewed_at")
    val viewedAt: String
)
