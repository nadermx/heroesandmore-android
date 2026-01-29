package com.heroesandmore.app.di

import com.heroesandmore.app.BuildConfig
import com.heroesandmore.app.data.api.*
import com.heroesandmore.app.data.local.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor {
        return AuthInterceptor(tokenManager)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMarketplaceApi(retrofit: Retrofit): MarketplaceApi {
        return retrofit.create(MarketplaceApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCollectionsApi(retrofit: Retrofit): CollectionsApi {
        return retrofit.create(CollectionsApi::class.java)
    }

    @Provides
    @Singleton
    fun providePricingApi(retrofit: Retrofit): PricingApi {
        return retrofit.create(PricingApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAlertsApi(retrofit: Retrofit): AlertsApi {
        return retrofit.create(AlertsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSocialApi(retrofit: Retrofit): SocialApi {
        return retrofit.create(SocialApi::class.java)
    }

    @Provides
    @Singleton
    fun provideScannerApi(retrofit: Retrofit): ScannerApi {
        return retrofit.create(ScannerApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSellerApi(retrofit: Retrofit): SellerApi {
        return retrofit.create(SellerApi::class.java)
    }

    @Provides
    @Singleton
    fun provideItemsApi(retrofit: Retrofit): ItemsApi {
        return retrofit.create(ItemsApi::class.java)
    }
}
