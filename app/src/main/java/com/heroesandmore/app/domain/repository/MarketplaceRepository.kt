package com.heroesandmore.app.domain.repository

import com.heroesandmore.app.data.dto.marketplace.*
import com.heroesandmore.app.domain.model.*
import com.heroesandmore.app.util.Resource
import java.io.File

interface MarketplaceRepository {
    // Listings
    suspend fun getListings(
        page: Int = 1,
        category: String? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        condition: String? = null,
        type: String? = null,
        sort: String? = null,
        search: String? = null
    ): Resource<List<Listing>>

    suspend fun getListingDetail(id: Int): Resource<ListingDetail>
    suspend fun createListing(request: CreateListingRequest): Resource<Listing>
    suspend fun updateListing(id: Int, request: UpdateListingRequest): Resource<Listing>
    suspend fun deleteListing(id: Int): Resource<Boolean>
    suspend fun publishListing(id: Int): Resource<Listing>
    suspend fun uploadListingImage(listingId: Int, imageFile: File): Resource<ListingImageDto>
    suspend fun deleteListingImage(listingId: Int, imageId: Int): Resource<Boolean>

    // Bids
    suspend fun placeBid(listingId: Int, amount: String): Resource<Bid>
    suspend fun getBidHistory(listingId: Int): Resource<List<Bid>>

    // Offers
    suspend fun makeOffer(listingId: Int, amount: String, message: String?): Resource<Offer>
    suspend fun getOffers(): Resource<List<Offer>>
    suspend fun acceptOffer(offerId: Int): Resource<Offer>
    suspend fun declineOffer(offerId: Int): Resource<Offer>
    suspend fun counterOffer(offerId: Int, amount: String): Resource<Offer>
    suspend fun acceptCounterOffer(offerId: Int): Resource<Int>  // Returns orderId
    suspend fun declineCounterOffer(offerId: Int): Resource<Boolean>

    // Saved Listings
    suspend fun isListingSaved(listingId: Int): Resource<Boolean>
    suspend fun saveListing(listingId: Int): Resource<Boolean>
    suspend fun unsaveListing(listingId: Int): Resource<Boolean>
    suspend fun getSavedListings(page: Int = 1): Resource<List<Listing>>

    // Orders
    suspend fun getOrders(page: Int = 1): Resource<List<Order>>
    suspend fun getOrderDetail(id: Int): Resource<Order>
    suspend fun shipOrder(orderId: Int, trackingNumber: String, carrier: String): Resource<Order>
    suspend fun confirmReceived(orderId: Int): Resource<Order>
    suspend fun createReview(orderId: Int, rating: Int, text: String?): Resource<Review>

    // Checkout
    suspend fun checkout(listingId: Int, shippingAddress: ShippingAddressDto, quantity: Int = 1): Resource<Order>
    suspend fun createPaymentIntent(listingId: Int, offerId: Int? = null, quantity: Int = 1): Resource<PaymentIntentResponse>
    suspend fun confirmPayment(paymentIntentId: String): Resource<Order>

    // Auctions
    suspend fun getAuctionEvents(): Resource<List<AuctionEventDto>>
    suspend fun getAuctionEventDetail(id: Int): Resource<AuctionEventDto>
    suspend fun getAuctionLots(eventId: Int, page: Int = 1): Resource<List<Listing>>
    suspend fun setAutoBid(listingId: Int, maxAmount: String): Resource<AutoBidDto>
    suspend fun getAutoBids(): Resource<List<AutoBidDto>>
    suspend fun cancelAutoBid(id: Int): Resource<Boolean>
    suspend fun getEndingSoon(): Resource<List<Listing>>
}
