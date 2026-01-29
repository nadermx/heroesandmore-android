package com.heroesandmore.app.domain.repository

import com.heroesandmore.app.domain.model.*
import com.heroesandmore.app.util.Resource

interface PricingRepository {
    suspend fun getPriceGuideItems(
        page: Int = 1,
        category: String? = null,
        search: String? = null,
        sort: String? = null
    ): Resource<List<PriceGuideItem>>

    suspend fun getPriceGuideItemDetail(id: Int): Resource<PriceGuideItemDetail>
    suspend fun getGradePrices(itemId: Int): Resource<List<GradePrice>>
    suspend fun getRecentSales(itemId: Int, page: Int = 1): Resource<List<Sale>>
    suspend fun getPriceHistory(itemId: Int, days: Int = 365): Resource<List<PriceHistory>>
    suspend fun searchPriceGuide(query: String): Resource<List<PriceGuideItem>>
    suspend fun getTrendingItems(): Resource<List<TrendingItem>>
    suspend fun getPriceGuideCategories(): Resource<List<Category>>
}
