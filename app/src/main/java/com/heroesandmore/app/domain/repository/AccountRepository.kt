package com.heroesandmore.app.domain.repository

import com.heroesandmore.app.data.dto.accounts.NotificationSettingsDto
import com.heroesandmore.app.data.dto.accounts.RecentlyViewedDto
import com.heroesandmore.app.domain.model.Listing
import com.heroesandmore.app.domain.model.PublicProfile
import com.heroesandmore.app.domain.model.Review
import com.heroesandmore.app.domain.model.User
import com.heroesandmore.app.domain.model.Collection
import com.heroesandmore.app.util.Resource
import java.io.File

interface AccountRepository {
    suspend fun getCurrentUser(): Resource<User>
    suspend fun updateProfile(bio: String?, location: String?, website: String?): Resource<User>
    suspend fun uploadAvatar(imageFile: File): Resource<User>
    suspend fun getNotificationSettings(): Resource<NotificationSettingsDto>
    suspend fun updateNotificationSettings(settings: Map<String, Boolean>): Resource<NotificationSettingsDto>
    suspend fun getPublicProfile(username: String): Resource<PublicProfile>
    suspend fun getUserListings(username: String, page: Int = 1): Resource<List<Listing>>
    suspend fun getUserCollections(username: String): Resource<List<Collection>>
    suspend fun getUserReviews(username: String, page: Int = 1): Resource<List<Review>>
    suspend fun getRecentlyViewed(): Resource<List<RecentlyViewedDto>>
    suspend fun clearRecentlyViewed(): Resource<Boolean>
    suspend fun registerDeviceToken(token: String): Resource<Boolean>
    suspend fun removeDeviceToken(token: String): Resource<Boolean>
}
