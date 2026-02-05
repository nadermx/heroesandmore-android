package com.heroesandmore.app.data.dto.marketplace

data class ReviewRequest(
    val rating: Int,
    val text: String? = null
)
