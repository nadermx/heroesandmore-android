package com.heroesandmore.app.data.repository

import com.heroesandmore.app.data.api.AuthApi
import com.heroesandmore.app.data.dto.accounts.*
import com.heroesandmore.app.domain.model.*
import com.heroesandmore.app.domain.repository.AccountRepository
import com.heroesandmore.app.util.Resource
import com.heroesandmore.app.util.safeApiCall
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepositoryImpl @Inject constructor(
    private val authApi: AuthApi
) : AccountRepository {

    override suspend fun getCurrentUser(): Resource<User> {
        val result = safeApiCall { authApi.getCurrentUser() }
        return result.map { it.toUser() }
    }

    override suspend fun updateProfile(bio: String?, location: String?, website: String?): Resource<User> {
        val result = safeApiCall { authApi.updateProfile(UpdateProfileRequest(bio, location, website)) }
        return result.map { it.toUser() }
    }

    override suspend fun uploadAvatar(imageFile: File): Resource<User> {
        val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("avatar", imageFile.name, requestBody)
        val result = safeApiCall { authApi.uploadAvatar(part) }
        return result.map { it.toUser() }
    }

    override suspend fun getNotificationSettings(): Resource<NotificationSettingsDto> {
        return safeApiCall { authApi.getNotificationSettings() }
    }

    override suspend fun updateNotificationSettings(settings: Map<String, Boolean>): Resource<NotificationSettingsDto> {
        val request = UpdateNotificationSettingsRequest(
            emailNewMessage = settings["email_new_message"],
            emailNewOffer = settings["email_new_offer"],
            emailOutbid = settings["email_outbid"],
            emailAuctionWon = settings["email_auction_won"],
            emailOrderShipped = settings["email_order_shipped"],
            emailPriceAlert = settings["email_price_alert"],
            emailWishlistMatch = settings["email_wishlist_match"],
            pushEnabled = settings["push_enabled"]
        )
        return safeApiCall { authApi.updateNotificationSettings(request) }
    }

    override suspend fun getPublicProfile(username: String): Resource<PublicProfile> {
        val result = safeApiCall { authApi.getPublicProfile(username) }
        return result.map { it.toPublicProfile() }
    }

    override suspend fun getUserListings(username: String, page: Int): Resource<List<Listing>> {
        val result = safeApiCall { authApi.getUserListings(username, page) }
        return result.map { response -> response.results.map { it.toListing() } }
    }

    override suspend fun getUserCollections(username: String): Resource<List<Collection>> {
        val result = safeApiCall { authApi.getUserCollections(username) }
        return result.map { collections -> collections.map { it.toCollection() } }
    }

    override suspend fun getUserReviews(username: String, page: Int): Resource<List<Review>> {
        val result = safeApiCall { authApi.getUserReviews(username, page) }
        return result.map { response -> response.results.map { it.toReview() } }
    }

    override suspend fun getRecentlyViewed(): Resource<List<RecentlyViewedDto>> {
        return safeApiCall { authApi.getRecentlyViewed() }
    }

    override suspend fun clearRecentlyViewed(): Resource<Boolean> {
        val result = safeApiCall { authApi.clearRecentlyViewed() }
        return when (result) {
            is Resource.Success -> Resource.success(true)
            is Resource.Error -> Resource.error(result.message ?: "Failed to clear recently viewed")
            is Resource.Loading -> Resource.loading()
        }
    }

    override suspend fun registerDeviceToken(token: String): Resource<Boolean> {
        val result = safeApiCall { authApi.registerDeviceToken(DeviceTokenRequest(token)) }
        return when (result) {
            is Resource.Success -> Resource.success(true)
            is Resource.Error -> Resource.error(result.message ?: "Failed to register device token")
            is Resource.Loading -> Resource.loading()
        }
    }

    override suspend fun removeDeviceToken(token: String): Resource<Boolean> {
        val result = safeApiCall { authApi.removeDeviceToken(token) }
        return when (result) {
            is Resource.Success -> Resource.success(true)
            is Resource.Error -> Resource.error(result.message ?: "Failed to remove device token")
            is Resource.Loading -> Resource.loading()
        }
    }

    // Mapping functions
    private fun ProfileDto.toUser(): User = User(
        id = id,
        username = username,
        email = email,
        avatarUrl = avatarUrl,
        bio = bio,
        location = location,
        website = website,
        isSellerVerified = isSellerVerified,
        rating = rating,
        stripeAccountComplete = stripeAccountComplete ?: false,
        created = created
    )

    private fun PublicProfileDto.toPublicProfile(): PublicProfile = PublicProfile(
        username = username,
        avatarUrl = avatarUrl,
        bio = bio,
        location = location,
        rating = rating,
        isSellerVerified = isSellerVerified,
        listingsCount = listingsCount,
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
        created = created
    )

    private fun com.heroesandmore.app.data.dto.collections.CollectionDto.toCollection(): Collection = Collection(
        id = id,
        name = name,
        description = description,
        isPublic = isPublic,
        ownerUsername = ownerUsername,
        itemCount = itemCount,
        totalValue = totalValue,
        totalCost = totalCost,
        gainLoss = gainLoss,
        created = created
    )

    private fun com.heroesandmore.app.data.dto.marketplace.ReviewDto.toReview(): Review = Review(
        id = id,
        orderId = order,
        rating = rating,
        text = text,
        reviewerUsername = reviewerUsername,
        created = created
    )
}
