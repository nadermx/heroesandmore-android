package com.heroesandmore.app.data.api

import com.heroesandmore.app.data.dto.common.PaginatedResponse
import com.heroesandmore.app.data.dto.marketplace.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface MarketplaceApi {

    // Listings
    @GET("marketplace/listings/")
    suspend fun getListings(
        @Query("page") page: Int = 1,
        @Query("category") category: String? = null,
        @Query("min_price") minPrice: Double? = null,
        @Query("max_price") maxPrice: Double? = null,
        @Query("condition") condition: String? = null,
        @Query("listing_type") type: String? = null,
        @Query("ordering") ordering: String? = null,
        @Query("search") search: String? = null
    ): Response<PaginatedResponse<ListingDto>>

    @GET("marketplace/listings/{id}/")
    suspend fun getListingDetail(@Path("id") id: Int): Response<ListingDetailDto>

    @POST("marketplace/listings/")
    suspend fun createListing(@Body listing: CreateListingRequest): Response<ListingDto>

    @PATCH("marketplace/listings/{id}/")
    suspend fun updateListing(
        @Path("id") id: Int,
        @Body listing: UpdateListingRequest
    ): Response<ListingDto>

    @DELETE("marketplace/listings/{id}/")
    suspend fun deleteListing(@Path("id") id: Int): Response<Unit>

    @POST("marketplace/listings/{id}/publish/")
    suspend fun publishListing(@Path("id") id: Int): Response<ListingDto>

    @Multipart
    @POST("marketplace/listings/{id}/images/")
    suspend fun uploadImage(
        @Path("id") listingId: Int,
        @Part image: MultipartBody.Part
    ): Response<ListingImageDto>

    @DELETE("marketplace/listings/{listingId}/images/{imageId}/")
    suspend fun deleteImage(
        @Path("listingId") listingId: Int,
        @Path("imageId") imageId: Int
    ): Response<Unit>

    // Bids
    @POST("marketplace/listings/{id}/bid/")
    suspend fun placeBid(
        @Path("id") listingId: Int,
        @Body bid: BidRequest
    ): Response<BidDto>

    @GET("marketplace/listings/{id}/bids/")
    suspend fun getBidHistory(@Path("id") listingId: Int): Response<List<BidDto>>

    // Offers
    @POST("marketplace/listings/{id}/offer/")
    suspend fun makeOffer(
        @Path("id") listingId: Int,
        @Body offer: OfferRequest
    ): Response<OfferDto>

    @GET("marketplace/offers/")
    suspend fun getOffers(): Response<PaginatedResponse<OfferDto>>

    @POST("marketplace/offers/{id}/accept/")
    suspend fun acceptOffer(@Path("id") offerId: Int): Response<OfferDto>

    @POST("marketplace/offers/{id}/decline/")
    suspend fun declineOffer(@Path("id") offerId: Int): Response<OfferDto>

    @POST("marketplace/offers/{id}/counter/")
    suspend fun counterOffer(
        @Path("id") offerId: Int,
        @Body counter: CounterOfferRequest
    ): Response<OfferDto>

    // Save/Unsave
    @GET("marketplace/listings/{id}/saved/")
    suspend fun isListingSaved(@Path("id") listingId: Int): Response<SavedStatusResponse>

    @POST("marketplace/listings/{id}/save/")
    suspend fun saveListing(@Path("id") listingId: Int): Response<StatusResponse>

    @DELETE("marketplace/listings/{id}/save/")
    suspend fun unsaveListing(@Path("id") listingId: Int): Response<Unit>

    @GET("marketplace/saved/")
    suspend fun getSavedListings(
        @Query("page") page: Int = 1
    ): Response<PaginatedResponse<ListingDto>>

    // Orders
    @GET("marketplace/orders/")
    suspend fun getOrders(
        @Query("page") page: Int = 1,
        @Query("role") role: String? = null,
        @Query("status") status: String? = null
    ): Response<PaginatedResponse<OrderDto>>

    @GET("marketplace/orders/{id}/")
    suspend fun getOrderDetail(@Path("id") orderId: Int): Response<OrderDto>

    @POST("marketplace/orders/{id}/ship/")
    suspend fun shipOrder(
        @Path("id") orderId: Int,
        @Body shipment: ShipOrderRequest
    ): Response<OrderDto>

    @POST("marketplace/orders/{id}/received/")
    suspend fun confirmReceived(@Path("id") orderId: Int): Response<OrderDto>

    @POST("marketplace/orders/{id}/review/")
    suspend fun createReview(
        @Path("id") orderId: Int,
        @Body review: CreateReviewRequest
    ): Response<ReviewDto>

    // Checkout
    @POST("marketplace/checkout/{listingId}/")
    suspend fun checkout(
        @Path("listingId") listingId: Int,
        @Body request: CheckoutRequest
    ): Response<OrderDto>

    @POST("marketplace/payment/intent/")
    suspend fun createPaymentIntent(@Body request: PaymentIntentRequest): Response<PaymentIntentResponse>

    @POST("marketplace/payment/confirm/")
    suspend fun confirmPayment(@Body request: ConfirmPaymentRequest): Response<OrderDto>

    // Auctions
    @GET("marketplace/auctions/events/")
    suspend fun getAuctionEvents(): Response<List<AuctionEventDto>>

    @GET("marketplace/auctions/events/{id}/")
    suspend fun getAuctionEventDetail(@Path("id") id: Int): Response<AuctionEventDto>

    @GET("marketplace/auctions/events/{eventId}/lots/")
    suspend fun getAuctionLots(
        @Path("eventId") eventId: Int,
        @Query("page") page: Int = 1
    ): Response<PaginatedResponse<ListingDto>>

    @GET("marketplace/auctions/ending-soon/")
    suspend fun getEndingSoon(): Response<List<ListingDto>>

    // Auto-bid
    @POST("marketplace/listings/{id}/autobid/")
    suspend fun setAutoBid(
        @Path("id") listingId: Int,
        @Body request: AutoBidRequest
    ): Response<AutoBidDto>

    @GET("marketplace/auctions/autobid/")
    suspend fun getAutoBids(): Response<List<AutoBidDto>>

    @DELETE("marketplace/auctions/autobid/{id}/")
    suspend fun cancelAutoBid(@Path("id") id: Int): Response<Unit>
}
