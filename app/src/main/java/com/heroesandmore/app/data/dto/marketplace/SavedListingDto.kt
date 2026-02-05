package com.heroesandmore.app.data.dto.marketplace

import com.google.gson.annotations.SerializedName

data class SavedListingDto(
    val id: Int,
    val listing: ListingDto,
    @SerializedName("saved_at")
    val savedAt: String
) {
    fun toListing(): ListingDto = listing
}
