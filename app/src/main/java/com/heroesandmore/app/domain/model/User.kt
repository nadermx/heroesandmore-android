package com.heroesandmore.app.domain.model

data class User(
    val id: Int,
    val username: String,
    val email: String?,
    val avatarUrl: String?,
    val bio: String?,
    val location: String?,
    val website: String?,
    val isSellerVerified: Boolean,
    val rating: Double?,
    val stripeAccountComplete: Boolean,
    val created: String
)

data class PublicProfile(
    val username: String,
    val avatarUrl: String?,
    val bio: String?,
    val location: String?,
    val rating: Double?,
    val isSellerVerified: Boolean,
    val listingsCount: Int,
    val created: String
)

data class AuthState(
    val isLoggedIn: Boolean,
    val user: User? = null,
    val accessToken: String? = null
)
