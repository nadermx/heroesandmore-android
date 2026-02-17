package com.heroesandmore.app.domain.model

data class AuctionEvent(
    val id: Int,
    val title: String,
    val description: String?,
    val startTime: String,
    val endTime: String,
    val lotCount: Int,
    val isLive: Boolean,
    val status: String,
    val created: String,
    val slug: String?,
    val isPlatformEvent: Boolean = false,
    val cadence: String? = null,
    val acceptingSubmissions: Boolean = false,
    val submissionDeadline: String? = null
)

data class AuctionLotSubmission(
    val id: Int,
    val listingId: Int,
    val listingTitle: String,
    val listingImage: String?,
    val eventName: String,
    val eventSlug: String,
    val status: String,
    val staffNotes: String?,
    val submittedAt: String,
    val reviewedAt: String?
)
