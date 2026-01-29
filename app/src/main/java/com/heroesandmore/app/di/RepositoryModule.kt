package com.heroesandmore.app.di

import com.heroesandmore.app.data.api.*
import com.heroesandmore.app.data.local.TokenManager
import com.heroesandmore.app.data.repository.*
import com.heroesandmore.app.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApi: AuthApi,
        tokenManager: TokenManager
    ): AuthRepository {
        return AuthRepositoryImpl(authApi, tokenManager)
    }

    @Provides
    @Singleton
    fun provideAccountRepository(
        authApi: AuthApi
    ): AccountRepository {
        return AccountRepositoryImpl(authApi)
    }

    @Provides
    @Singleton
    fun provideMarketplaceRepository(
        marketplaceApi: MarketplaceApi
    ): MarketplaceRepository {
        return MarketplaceRepositoryImpl(marketplaceApi)
    }

    @Provides
    @Singleton
    fun provideCollectionRepository(
        collectionsApi: CollectionsApi
    ): CollectionRepository {
        return CollectionRepositoryImpl(collectionsApi)
    }

    @Provides
    @Singleton
    fun provideItemsRepository(
        itemsApi: ItemsApi
    ): ItemsRepository {
        return ItemsRepositoryImpl(itemsApi)
    }

    @Provides
    @Singleton
    fun provideAlertsRepository(
        alertsApi: AlertsApi
    ): AlertsRepository {
        return AlertsRepositoryImpl(alertsApi)
    }

    @Provides
    @Singleton
    fun provideSocialRepository(
        socialApi: SocialApi
    ): SocialRepository {
        return SocialRepositoryImpl(socialApi)
    }

    @Provides
    @Singleton
    fun provideScannerRepository(
        scannerApi: ScannerApi
    ): ScannerRepository {
        return ScannerRepositoryImpl(scannerApi)
    }

    @Provides
    @Singleton
    fun provideSellerRepository(
        sellerApi: SellerApi
    ): SellerRepository {
        return SellerRepositoryImpl(sellerApi)
    }
}
