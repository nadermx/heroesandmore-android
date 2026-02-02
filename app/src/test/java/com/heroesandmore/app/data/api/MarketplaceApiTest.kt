package com.heroesandmore.app.data.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.heroesandmore.app.data.dto.marketplace.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MarketplaceApiTest {

    private lateinit var gson: Gson

    @Before
    fun setup() {
        gson = GsonBuilder().create()
    }

    // MARK: - Listing DTO Tests

    @Test
    fun `test listing dto deserialization`() {
        val json = """
        {
            "id": 1,
            "title": "Test Card",
            "description": "A test listing",
            "price": "99.99",
            "current_bid": null,
            "condition": "mint",
            "listing_type": "fixed",
            "status": "active",
            "bid_count": 0,
            "view_count": 100,
            "seller": {
                "username": "seller1",
                "rating": 4.5
            }
        }
        """.trimIndent()

        val listing = gson.fromJson(json, ListingDto::class.java)

        assertEquals(1, listing.id)
        assertEquals("Test Card", listing.title)
        assertEquals("99.99", listing.price)
        assertEquals("mint", listing.condition)
        assertEquals("fixed", listing.listingType)
        assertEquals(0, listing.bidCount)
    }

    @Test
    fun `test bid dto deserialization`() {
        val json = """
        {
            "id": 1,
            "amount": "150.00",
            "bidder": "buyer1",
            "created": "2026-01-15T12:00:00Z",
            "is_winning": true
        }
        """.trimIndent()

        val bid = gson.fromJson(json, BidDto::class.java)

        assertEquals(1, bid.id)
        assertEquals("150.00", bid.amount)
        assertEquals("buyer1", bid.bidder)
        assertTrue(bid.isWinning)
    }

    @Test
    fun `test offer dto deserialization`() {
        val json = """
        {
            "id": 1,
            "listing_id": 5,
            "amount": "175.00",
            "message": "Would you accept $175?",
            "status": "pending",
            "is_from_buyer": true,
            "created": "2026-01-15T14:00:00Z"
        }
        """.trimIndent()

        val offer = gson.fromJson(json, OfferDto::class.java)

        assertEquals(1, offer.id)
        assertEquals("175.00", offer.amount)
        assertEquals("pending", offer.status)
        assertTrue(offer.isFromBuyer)
    }

    @Test
    fun `test auto bid dto deserialization`() {
        val json = """
        {
            "id": 1,
            "listing": 10,
            "listing_title": "Auction Item",
            "max_amount": "100.00",
            "is_active": true,
            "created": "2026-01-15T09:00:00Z"
        }
        """.trimIndent()

        val autoBid = gson.fromJson(json, AutoBidDto::class.java)

        assertEquals(1, autoBid.id)
        assertEquals(10, autoBid.listing)
        assertEquals("Auction Item", autoBid.listingTitle)
        assertEquals("100.00", autoBid.maxAmount)
        assertTrue(autoBid.isActive)
    }

    @Test
    fun `test checkout response deserialization`() {
        val json = """
        {
            "order_id": 123,
            "total": "104.99",
            "subtotal": "99.99",
            "shipping": "5.00"
        }
        """.trimIndent()

        val response = gson.fromJson(json, CheckoutResponse::class.java)

        assertEquals(123, response.orderId)
        assertEquals("104.99", response.total)
        assertEquals("99.99", response.subtotal)
    }

    @Test
    fun `test payment intent response deserialization`() {
        val json = """
        {
            "client_secret": "pi_secret_123",
            "payment_intent_id": "pi_123",
            "amount": 10499,
            "currency": "usd"
        }
        """.trimIndent()

        val response = gson.fromJson(json, PaymentIntentResponse::class.java)

        assertEquals("pi_secret_123", response.clientSecret)
        assertEquals("pi_123", response.paymentIntentId)
        assertEquals(10499, response.amount)
        assertEquals("usd", response.currency)
    }

    // MARK: - Request Serialization Tests

    @Test
    fun `test bid request serialization`() {
        val request = BidRequest("150.00")
        val json = gson.toJson(request)

        assertTrue(json.contains("150.00"))
    }

    @Test
    fun `test auto bid request serialization`() {
        val request = AutoBidRequest("200.00")
        val json = gson.toJson(request)

        assertTrue(json.contains("max_amount"))
        assertTrue(json.contains("200.00"))
    }

    @Test
    fun `test offer request serialization`() {
        val request = OfferRequest("175.00", "Would you accept this?")
        val json = gson.toJson(request)

        assertTrue(json.contains("175.00"))
        assertTrue(json.contains("Would you accept this?"))
    }

    @Test
    fun `test create listing request serialization`() {
        val request = CreateListingRequest(
            title = "Test Card",
            description = "Description",
            price = "99.99",
            categoryId = 1,
            listingType = "fixed",
            condition = "mint"
        )
        val json = gson.toJson(request)

        assertTrue(json.contains("Test Card"))
        assertTrue(json.contains("99.99"))
        assertTrue(json.contains("category_id"))
    }

    // MARK: - Order DTO Tests

    @Test
    fun `test order dto deserialization`() {
        val json = """
        {
            "id": 1,
            "listing_id": 5,
            "listing_title": "Test Item",
            "buyer": "buyer1",
            "seller": "seller1",
            "total": "104.99",
            "status": "pending",
            "created": "2026-01-15T10:00:00Z"
        }
        """.trimIndent()

        val order = gson.fromJson(json, OrderDto::class.java)

        assertEquals(1, order.id)
        assertEquals("Test Item", order.listingTitle)
        assertEquals("pending", order.status)
        assertEquals("104.99", order.total)
    }

    // MARK: - Auction Event Tests

    @Test
    fun `test auction event dto deserialization`() {
        val json = """
        {
            "id": 1,
            "title": "Weekly Auction",
            "description": "Our weekly sports card auction",
            "start_date": "2026-01-20T18:00:00Z",
            "end_date": "2026-01-21T21:00:00Z",
            "status": "upcoming",
            "listing_count": 50
        }
        """.trimIndent()

        val event = gson.fromJson(json, AuctionEventDto::class.java)

        assertEquals(1, event.id)
        assertEquals("Weekly Auction", event.title)
        assertEquals("upcoming", event.status)
        assertEquals(50, event.listingCount)
    }
}
