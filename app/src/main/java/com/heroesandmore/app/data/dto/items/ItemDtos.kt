package com.heroesandmore.app.data.dto.items

import com.google.gson.annotations.SerializedName

data class CategoryDto(
    val id: Int,
    val name: String,
    val slug: String,
    val description: String?,
    val icon: String?,
    @SerializedName("parent_id")
    val parentId: Int?,
    @SerializedName("listing_count")
    val listingCount: Int?,
    val children: List<CategoryDto>?
)

data class CategoryListDto(
    val id: Int,
    val name: String,
    val slug: String,
    @SerializedName("full_path")
    val fullPath: String?
)

data class SearchResultDto(
    val id: Int,
    val type: String,
    val title: String,
    val subtitle: String?,
    val image: String?,
    val url: String?,
    @SerializedName("listing_id")
    val listingId: Int?,
    @SerializedName("price_guide_id")
    val priceGuideId: Int?,
    @SerializedName("category_slug")
    val categorySlug: String?
)

data class AutocompleteDto(
    val id: Int,
    val text: String,
    val type: String,
    val image: String?,
    @SerializedName("listing_id")
    val listingId: Int?,
    @SerializedName("category_slug")
    val categorySlug: String?
)
