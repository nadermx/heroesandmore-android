package com.heroesandmore.app.domain.repository

import com.heroesandmore.app.domain.model.*
import com.heroesandmore.app.util.Resource

interface ItemsRepository {
    suspend fun getCategoryTree(): Resource<List<Category>>
    suspend fun getCategoryList(): Resource<List<CategoryListItem>>
    suspend fun getCategory(slug: String): Resource<Category>
    suspend fun getCategoryListings(slug: String, page: Int = 1): Resource<List<Listing>>
    suspend fun globalSearch(query: String): Resource<List<SearchResult>>
    suspend fun autocomplete(query: String): Resource<List<AutocompleteResult>>
}
