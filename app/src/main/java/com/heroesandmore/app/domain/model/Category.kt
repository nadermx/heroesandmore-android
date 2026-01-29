package com.heroesandmore.app.domain.model

data class Category(
    val id: Int,
    val name: String,
    val slug: String,
    val description: String?,
    val icon: String?,
    val parentId: Int?,
    val listingCount: Int?,
    val children: List<Category>?
)

data class CategoryListItem(
    val id: Int,
    val name: String,
    val slug: String,
    val fullPath: String?
)

data class SearchResult(
    val id: Int,
    val type: SearchResultType,
    val title: String,
    val subtitle: String?,
    val image: String?,
    val listingId: Int?,
    val priceGuideId: Int?,
    val categorySlug: String?
)

enum class SearchResultType {
    LISTING,
    PRICE_GUIDE,
    CATEGORY,
    USER;

    companion object {
        fun fromString(value: String): SearchResultType {
            return when (value.lowercase()) {
                "listing" -> LISTING
                "price_guide" -> PRICE_GUIDE
                "category" -> CATEGORY
                "user" -> USER
                else -> LISTING
            }
        }
    }
}

data class AutocompleteResult(
    val id: Int,
    val text: String,
    val type: String,
    val image: String?,
    val listingId: Int?,
    val categorySlug: String?
)
