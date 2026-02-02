package com.heroesandmore.app.data.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.heroesandmore.app.data.dto.auth.*
import com.heroesandmore.app.data.dto.accounts.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AuthApiTest {

    private lateinit var gson: Gson

    @Before
    fun setup() {
        gson = GsonBuilder().create()
    }

    // MARK: - Token Tests

    @Test
    fun `test token response deserialization`() {
        val json = """
        {
            "access": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.access",
            "refresh": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.refresh"
        }
        """.trimIndent()

        val response = gson.fromJson(json, TokenResponse::class.java)

        assertTrue(response.access.startsWith("eyJ"))
        assertTrue(response.refresh.startsWith("eyJ"))
    }

    @Test
    fun `test login request serialization`() {
        val request = LoginRequest("testuser", "password123")
        val json = gson.toJson(request)

        assertTrue(json.contains("testuser"))
        assertTrue(json.contains("password123"))
    }

    @Test
    fun `test refresh token request serialization`() {
        val request = RefreshTokenRequest("refresh_token_here")
        val json = gson.toJson(request)

        assertTrue(json.contains("refresh_token_here"))
    }

    // MARK: - Registration Tests

    @Test
    fun `test register request serialization`() {
        val request = RegisterRequest(
            username = "newuser",
            email = "new@example.com",
            password = "securepass123",
            passwordConfirm = "securepass123"
        )
        val json = gson.toJson(request)

        assertTrue(json.contains("newuser"))
        assertTrue(json.contains("new@example.com"))
        assertTrue(json.contains("password_confirm"))
    }

    @Test
    fun `test register response deserialization`() {
        val json = """
        {
            "user": {
                "id": 1,
                "username": "newuser",
                "email": "new@example.com"
            },
            "tokens": {
                "access": "access_token",
                "refresh": "refresh_token"
            }
        }
        """.trimIndent()

        val response = gson.fromJson(json, RegisterResponse::class.java)

        assertEquals("newuser", response.user.username)
        assertEquals("access_token", response.tokens.access)
    }

    // MARK: - Profile Tests

    @Test
    fun `test profile dto deserialization`() {
        val json = """
        {
            "id": 1,
            "username": "seller1",
            "email": "seller@example.com",
            "avatar_url": "https://example.com/avatar.jpg",
            "bio": "Avid collector",
            "location": "New York, NY",
            "website": "https://mycollection.com",
            "is_seller_verified": true,
            "stripe_account_complete": true,
            "seller_tier": "premium",
            "rating": 4.8,
            "rating_count": 150,
            "total_sales_count": 500,
            "is_public": true
        }
        """.trimIndent()

        val profile = gson.fromJson(json, ProfileDto::class.java)

        assertEquals(1, profile.id)
        assertEquals("seller1", profile.username)
        assertEquals("Avid collector", profile.bio)
        assertTrue(profile.isSellerVerified)
        assertEquals("premium", profile.sellerTier)
        assertEquals(4.8, profile.rating!!, 0.01)
    }

    @Test
    fun `test update profile request serialization`() {
        val request = UpdateProfileRequest(
            bio = "Updated bio",
            location = "Los Angeles, CA",
            website = "https://newsite.com"
        )
        val json = gson.toJson(request)

        assertTrue(json.contains("Updated bio"))
        assertTrue(json.contains("Los Angeles, CA"))
    }

    // MARK: - Notification Settings Tests

    @Test
    fun `test notification settings dto deserialization`() {
        val json = """
        {
            "email_notifications": true,
            "push_new_bid": true,
            "push_outbid": true,
            "push_offer": false,
            "push_order_shipped": true,
            "push_message": true,
            "push_price_alert": false
        }
        """.trimIndent()

        val settings = gson.fromJson(json, NotificationSettingsDto::class.java)

        assertTrue(settings.emailNotifications)
        assertTrue(settings.pushNewBid)
        assertTrue(settings.pushOutbid)
        assertFalse(settings.pushOffer)
        assertTrue(settings.pushOrderShipped)
        assertTrue(settings.pushMessage)
        assertFalse(settings.pushPriceAlert)
    }

    @Test
    fun `test update notification settings request serialization`() {
        val request = UpdateNotificationSettingsRequest(
            emailNotifications = false,
            pushNewBid = true,
            pushOutbid = false
        )
        val json = gson.toJson(request)

        assertTrue(json.contains("email_notifications"))
        assertTrue(json.contains("push_new_bid"))
    }

    // MARK: - Password Tests

    @Test
    fun `test password reset request serialization`() {
        val request = PasswordResetRequest("user@example.com")
        val json = gson.toJson(request)

        assertTrue(json.contains("user@example.com"))
    }

    @Test
    fun `test password reset confirm request serialization`() {
        val request = PasswordResetConfirmRequest(
            uid = "uid123",
            token = "token456",
            newPassword = "newpassword",
            newPasswordConfirm = "newpassword"
        )
        val json = gson.toJson(request)

        assertTrue(json.contains("uid123"))
        assertTrue(json.contains("token456"))
        assertTrue(json.contains("new_password"))
    }

    @Test
    fun `test change password request serialization`() {
        val request = ChangePasswordRequest(
            oldPassword = "oldpass",
            newPassword = "newpass",
            newPasswordConfirm = "newpass"
        )
        val json = gson.toJson(request)

        assertTrue(json.contains("old_password"))
        assertTrue(json.contains("new_password"))
    }

    // MARK: - Google Auth Tests

    @Test
    fun `test google auth request serialization`() {
        val request = GoogleAuthRequest("google_id_token_here")
        val json = gson.toJson(request)

        assertTrue(json.contains("id_token"))
        assertTrue(json.contains("google_id_token_here"))
    }

    // MARK: - Device Token Tests

    @Test
    fun `test device token request serialization`() {
        val request = DeviceTokenRequest(
            token = "fcm_token_123",
            deviceType = "android"
        )
        val json = gson.toJson(request)

        assertTrue(json.contains("fcm_token_123"))
        assertTrue(json.contains("android"))
    }

    // MARK: - Public Profile Tests

    @Test
    fun `test public profile dto deserialization`() {
        val json = """
        {
            "username": "publicuser",
            "avatar_url": "https://example.com/avatar.jpg",
            "bio": "Public bio",
            "location": "Miami, FL",
            "rating": 4.5,
            "rating_count": 50,
            "is_seller_verified": false,
            "total_sales_count": 25,
            "listings_count": 10
        }
        """.trimIndent()

        val profile = gson.fromJson(json, PublicProfileDto::class.java)

        assertEquals("publicuser", profile.username)
        assertEquals("Miami, FL", profile.location)
        assertFalse(profile.isSellerVerified)
        assertEquals(10, profile.listingsCount)
    }
}
