package com.heroesandmore.app.data.repository

import com.heroesandmore.app.data.api.MarketplaceApi
import com.heroesandmore.app.data.dto.marketplace.*
import com.heroesandmore.app.domain.model.*
import com.heroesandmore.app.domain.repository.MarketplaceRepository
import com.heroesandmore.app.util.Resource
import com.heroesandmore.app.util.safeApiCall
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarketplaceRepositoryImpl @Inject constructor(
    private val marketplaceApi: MarketplaceApi
) : MarketplaceRepository {

    override suspend fun getListings(
        page: Int,
        category: String?,
        minPrice: Double?,
        maxPrice: Double?,
        condition: String?,
        type: String?,
        sort: String?,
        search: String?
    ): Resource<List<Listing>> {
        val result = safeApiCall {
            marketplaceApi.getListings(page, category, minPrice, maxPrice, condition, type, sort, search)
        }
        return result.map { response -> response.results.map { it.toListing() } }
    }

    override suspend fun getListingDetail(id: Int): Resource<ListingDetail> {
        val result = safeApiCall { marketplaceApi.getListingDetail(id) }
        return result.map { it.toListingDetail() }
    }

    override suspend fun createListing(request: CreateListingRequest): Resource<Listing> {
        val result = safeApiCall { marketplaceApi.createListing(request) }
        return result.map { it.toListing() }
    }

    override suspend fun updateListing(id: Int, request: UpdateListingRequest): Resource<Listing> {
        val result = safeApiCall { marketplaceApi.updateListing(id, request) }
        return result.map { it.toListing() }
    }

    override suspend fun deleteListing(id: Int): Resource<Boolean> {
        val result = safeApiCall { marketplaceApi.deleteListing(id) }
        return when (result) {
            is Resource.Success -> Resource.success(true)
            is Resource.Error -> Resource.error(result.message ?: "Failed to delete listing")
            is Resource.Loading -> Resource.loading()
            else -> Resource.error("Unknown error")
        }
    }

    override suspend fun publishListing(id: Int): Resource<Listing> {
        val result = safeApiCall { marketplaceApi.publishListing(id) }
        return result.map { it.toListing() }
    }

    override suspend fun uploadListingImage(listingId: Int, imageFile: File): Resource<ListingImageDto> {
        val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("image", imageFile.name, requestBody)
        return safeApiCall { marketplaceApi.uploadImage(listingId, part) }
    }

    override suspend fun deleteListingImage(listingId: Int, imageId: Int): Resource<Boolean> {
        val result = safeApiCall { marketplaceApi.deleteImage(listingId, imageId) }
        return when (result) {
            is Resource.Success -> Resource.success(true)
            is Resource.Error -> Resource.error(result.message ?: "Failed to delete image")
            is Resource.Loading -> Resource.loading()
            else -> Resource.error("Unknown error")
        }
    }

    override suspend fun placeBid(listingId: Int, amount: String): Resource<Bid> {
        val result = safeApiCall { marketplaceApi.placeBid(listingId, BidRequest(amount)) }
        return result.map { it.toBid() }
    }

    override suspend fun getBidHistory(listingId: Int): Resource<List<Bid>> {
        val result = safeApiCall { marketplaceApi.getBidHistory(listingId) }
        return result.map { bids -> bids.map { it.toBid() } }
    }

    override suspend fun makeOffer(listingId: Int, amount: String, message: String?): Resource<Offer> {
        val result = safeApiCall { marketplaceApi.makeOffer(listingId, OfferRequest(amount, message)) }
        return result.map { it.toOffer() }
    }

    override suspend fun getOffers(): Resource<List<Offer>> {
        val result = safeApiCall { marketplaceApi.getOffers() }
        return result.map { response -> response.results.map { it.toOffer() } }
    }

    override suspend fun acceptOffer(offerId: Int): Resource<Offer> {
        val result = safeApiCall { marketplaceApi.acceptOffer(offerId) }
        return result.map { it.toOffer() }
    }

    override suspend fun declineOffer(offerId: Int): Resource<Offer> {
        val result = safeApiCall { marketplaceApi.declineOffer(offerId) }
        return result.map { it.toOffer() }
    }

    override suspend fun counterOffer(offerId: Int, amount: String): Resource<Offer> {
        val result = safeApiCall { marketplaceApi.counterOffer(offerId, CounterOfferRequest(amount)) }
        return result.map { it.toOffer() }
    }

    override suspend fun acceptCounterOffer(offerId: Int): Resource<Int> {
        val result = safeApiCall { marketplaceApi.acceptCounterOffer(offerId) }
        return result.map { it.orderId }
    }

    override suspend fun declineCounterOffer(offerId: Int): Resource<Boolean> {
        val result = safeApiCall { marketplaceApi.declineCounterOffer(offerId) }
        return when (result) {
            is Resource.Success -> Resource.success(true)
            is Resource.Error -> Resource.error(result.message ?: "Failed to decline counter-offer")
            is Resource.Loading -> Resource.loading()
            else -> Resource.error("Unknown error")
        }
    }

    override suspend fun isListingSaved(listingId: Int): Resource<Boolean> {
        val result = safeApiCall { marketplaceApi.isListingSaved(listingId) }
        return result.map { it.isSaved }
    }

    override suspend fun saveListing(listingId: Int): Resource<Boolean> {
        val result = safeApiCall { marketplaceApi.saveListing(listingId) }
        return when (result) {
            is Resource.Success -> Resource.success(true)
            is Resource.Error -> Resource.error(result.message ?: "Failed to save listing")
            is Resource.Loading -> Resource.loading()
            else -> Resource.error("Unknown error")
        }
    }

    override suspend fun unsaveListing(listingId: Int): Resource<Boolean> {
        val result = safeApiCall { marketplaceApi.unsaveListing(listingId) }
        return when (result) {
            is Resource.Success -> Resource.success(true)
            is Resource.Error -> Resource.error(result.message ?: "Failed to unsave listing")
            is Resource.Loading -> Resource.loading()
            else -> Resource.error("Unknown error")
        }
    }

    override suspend fun getSavedListings(page: Int): Resource<List<Listing>> {
        val result = safeApiCall { marketplaceApi.getSavedListings(page) }
        return result.map { response -> response.results.map { it.toListing() } }
    }

    override suspend fun getOrders(page: Int): Resource<List<Order>> {
        val result = safeApiCall { marketplaceApi.getOrders(page) }
        return result.map { response -> response.results.map { it.toOrder() } }
    }

    override suspend fun getOrderDetail(id: Int): Resource<Order> {
        val result = safeApiCall { marketplaceApi.getOrderDetail(id) }
        return result.map { it.toOrder() }
    }

    override suspend fun shipOrder(orderId: Int, trackingNumber: String, carrier: String): Resource<Order> {
        val result = safeApiCall { marketplaceApi.shipOrder(orderId, ShipOrderRequest(trackingNumber, carrier)) }
        return result.map { it.toOrder() }
    }

    override suspend fun confirmReceived(orderId: Int): Resource<Order> {
        val result = safeApiCall { marketplaceApi.confirmReceived(orderId) }
        return result.map { it.toOrder() }
    }

    override suspend fun createReview(orderId: Int, rating: Int, text: String?): Resource<Review> {
        val result = safeApiCall { marketplaceApi.createReview(orderId, CreateReviewRequest(rating, text)) }
        return result.map { it.toReview() }
    }

    override suspend fun checkout(listingId: Int, shippingAddress: ShippingAddressDto, quantity: Int): Resource<Order> {
        val result = safeApiCall { marketplaceApi.checkout(listingId, CheckoutRequest(shippingAddress, quantity)) }
        return result.map { it.toOrder() }
    }

    override suspend fun createPaymentIntent(listingId: Int, offerId: Int?, quantity: Int): Resource<PaymentIntentResponse> {
        return safeApiCall { marketplaceApi.createPaymentIntent(PaymentIntentRequest(listingId, offerId, quantity)) }
    }

    override suspend fun confirmPayment(paymentIntentId: String): Resource<Order> {
        val result = safeApiCall { marketplaceApi.confirmPayment(ConfirmPaymentRequest(paymentIntentId)) }
        return result.map { it.toOrder() }
    }

    override suspend fun getAuctionEvents(): Resource<List<AuctionEventDto>> {
        return safeApiCall { marketplaceApi.getAuctionEvents() }
    }

    override suspend fun getAuctionEventDetail(id: Int): Resource<AuctionEventDto> {
        return safeApiCall { marketplaceApi.getAuctionEventDetail(id) }
    }

    override suspend fun getAuctionLots(eventId: Int, page: Int): Resource<List<Listing>> {
        val result = safeApiCall { marketplaceApi.getAuctionLots(eventId, page) }
        return result.map { response -> response.results.map { it.toListing() } }
    }

    override suspend fun setAutoBid(listingId: Int, maxAmount: String): Resource<AutoBidDto> {
        return safeApiCall { marketplaceApi.setAutoBid(listingId, AutoBidRequest(maxAmount)) }
    }

    override suspend fun getAutoBids(): Resource<List<AutoBidDto>> {
        return safeApiCall { marketplaceApi.getAutoBids() }
    }

    override suspend fun cancelAutoBid(id: Int): Resource<Boolean> {
        val result = safeApiCall { marketplaceApi.cancelAutoBid(id) }
        return when (result) {
            is Resource.Success -> Resource.success(true)
            is Resource.Error -> Resource.error(result.message ?: "Failed to cancel auto-bid")
            is Resource.Loading -> Resource.loading()
            else -> Resource.error("Unknown error")
        }
    }

    override suspend fun getEndingSoon(): Resource<List<Listing>> {
        val result = safeApiCall { marketplaceApi.getEndingSoon() }
        return result.map { listings -> listings.map { it.toListing() } }
    }

    override suspend fun getPlatformAuctionEvents(): Resource<List<AuctionEvent>> {
        val result = safeApiCall { marketplaceApi.getPlatformAuctionEvents() }
        return result.map { events -> events.map { it.toAuctionEvent() } }
    }

    override suspend fun getPlatformAuctionDetail(slug: String): Resource<AuctionEvent> {
        val result = safeApiCall { marketplaceApi.getPlatformAuctionDetail(slug) }
        return result.map { it.toAuctionEvent() }
    }

    override suspend fun getPlatformAuctionLots(slug: String, page: Int): Resource<List<Listing>> {
        val result = safeApiCall { marketplaceApi.getPlatformAuctionLots(slug, page) }
        return result.map { response -> response.results.map { it.toListing() } }
    }

    override suspend fun submitAuctionLot(eventSlug: String, listingId: Int): Resource<AuctionLotSubmission> {
        val result = safeApiCall { marketplaceApi.submitAuctionLot(eventSlug, SubmitLotRequest(listingId)) }
        return result.map { it.toAuctionLotSubmission() }
    }

    override suspend fun getMySubmissions(): Resource<List<AuctionLotSubmission>> {
        val result = safeApiCall { marketplaceApi.getMySubmissions() }
        return result.map { submissions -> submissions.map { it.toAuctionLotSubmission() } }
    }

    override suspend fun getMyActiveListings(): Resource<List<Listing>> {
        val result = safeApiCall { marketplaceApi.getMyActiveListings() }
        return result.map { response -> response.results.map { it.toListing() } }
    }

    // Mapping functions
    private fun ListingDto.toListing(): Listing = Listing(
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
        quantityAvailable = quantityAvailable ?: 1,
        isPlatformListing = isPlatformListing,
        sellerIsTrusted = sellerIsTrusted
    )

    private fun ListingDetailDto.toListingDetail(): ListingDetail = ListingDetail(
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
        quantity = quantity ?: 1,
        quantityAvailable = quantityAvailable ?: 1,
        quantitySold = quantitySold ?: 0,
        isPlatformListing = isPlatformListing
    )

    private fun com.heroesandmore.app.data.dto.accounts.PublicProfileDto.toPublicProfile(): PublicProfile = PublicProfile(
        username = username,
        avatarUrl = avatarUrl,
        bio = bio,
        location = location,
        rating = rating,
        isSellerVerified = isSellerVerified,
        isTrustedSeller = isTrustedSeller,
        listingsCount = listingsCount,
        created = created
    )

    private fun BidDto.toBid(): Bid = Bid(
        id = id,
        amount = amount,
        bidderUsername = bidderUsername,
        created = created
    )

    private fun OfferDto.toOffer(): Offer = Offer(
        id = id,
        listing = OfferListing(
            id = listing.id,
            title = listing.title,
            price = listing.price,
            imageUrl = listing.imageUrl
        ),
        amount = amount,
        message = message,
        buyerUsername = buyerUsername,
        status = OfferStatus.fromString(status),
        isFromBuyer = isFromBuyer,
        counterAmount = counterAmount,
        counterMessage = counterMessage,
        timeRemaining = timeRemaining,
        created = created
    )

    private fun OrderDto.toOrder(): Order = Order(
        id = id,
        listing = listing.toListing(),
        buyerUsername = buyerUsername,
        sellerUsername = sellerUsername,
        quantity = quantity ?: 1,
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

    private fun ReviewDto.toReview(): Review = Review(
        id = id,
        orderId = order,
        rating = rating,
        text = text,
        reviewerUsername = reviewerUsername,
        created = created
    )

    private fun AuctionEventDto.toAuctionEvent(): AuctionEvent = AuctionEvent(
        id = id,
        title = title,
        description = description,
        startTime = startTime,
        endTime = endTime,
        lotCount = lotCount,
        isLive = isLive,
        status = status,
        created = created,
        slug = slug,
        isPlatformEvent = isPlatformEvent,
        cadence = cadence,
        acceptingSubmissions = acceptingSubmissions,
        submissionDeadline = submissionDeadline
    )

    private fun AuctionLotSubmissionDto.toAuctionLotSubmission(): AuctionLotSubmission = AuctionLotSubmission(
        id = id,
        listingId = listingId,
        listingTitle = listingTitle,
        listingImage = listingImage,
        eventName = eventName,
        eventSlug = eventSlug,
        status = status,
        staffNotes = staffNotes,
        submittedAt = submittedAt,
        reviewedAt = reviewedAt
    )
}
