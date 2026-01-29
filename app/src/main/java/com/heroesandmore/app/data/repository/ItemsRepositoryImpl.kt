package com.heroesandmore.app.data.repository

import com.heroesandmore.app.data.api.ItemsApi
import com.heroesandmore.app.data.dto.items.*
import com.heroesandmore.app.domain.model.*
import com.heroesandmore.app.domain.repository.ItemsRepository
import com.heroesandmore.app.util.Resource
import com.heroesandmore.app.util.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemsRepositoryImpl @Inject constructor(
    private val itemsApi: ItemsApi
) : ItemsRepository {

    override suspend fun getCategoryTree(): Resource<List<Category>> {
        val result = safeApiCall { itemsApi.getCategoryTree() }
        return result.map { categories -> categories.map { it.toCategory() } }
    }

    override suspend fun getCategoryList(): Resource<List<CategoryListItem>> {
        val result = safeApiCall { itemsApi.getCategoryList() }
        return result.map { categories -> categories.map { it.toCategoryListItem() } }
    }

    override suspend fun getCategory(slug: String): Resource<Category> {
        val result = safeApiCall { itemsApi.getCategory(slug) }
        return result.map { it.toCategory() }
    }

    override suspend fun getCategoryListings(slug: String, page: Int): Resource<List<Listing>> {
        val result = safeApiCall { itemsApi.getCategoryListings(slug, page) }
        return result.map { response -> response.results.map { it.toListing() } }
    }

    override suspend fun globalSearch(query: String): Resource<List<SearchResult>> {
        val result = safeApiCall { itemsApi.globalSearch(query) }
        return result.map { results -> results.map { it.toSearchResult() } }
    }

    override suspend fun autocomplete(query: String): Resource<List<AutocompleteResult>> {
        val result = safeApiCall { itemsApi.autocomplete(query) }
        return result.map { results -> results.map { it.toAutocompleteResult() } }
    }

    // Mapping functions
    private fun CategoryDto.toCategory(): Category = Category(
        id = id,
        name = name,
        slug = slug,
        description = description,
        icon = icon,
        parentId = parentId,
        listingCount = listingCount,
        children = children?.map { it.toCategory() }
    )

    private fun CategoryListDto.toCategoryListItem(): CategoryListItem = CategoryListItem(
        id = id,
        name = name,
        slug = slug,
        fullPath = fullPath
    )

    private fun SearchResultDto.toSearchResult(): SearchResult = SearchResult(
        id = id,
        type = SearchResultType.fromString(type),
        title = title,
        subtitle = subtitle,
        image = image,
        listingId = listingId,
        priceGuideId = priceGuideId,
        categorySlug = categorySlug
    )

    private fun AutocompleteDto.toAutocompleteResult(): AutocompleteResult = AutocompleteResult(
        id = id,
        text = text,
        type = type,
        image = image,
        listingId = listingId,
        categorySlug = categorySlug
    )

    private fun com.heroesandmore.app.data.dto.marketplace.ListingDto.toListing(): Listing = Listing(
        id = id,
        title = title,
        price = price,
        currentPrice = currentPrice,
        listingType = ListingType.fromString(listingType),
        condition = condition,
        sellerUsername = sellerUsername,
        categoryName = categoryName,
        primaryImage = primaryImage,
        auctionEnd = auctionEnd,
        timeRemaining = timeRemaining,
        views = views,
        created = created
    )
}
