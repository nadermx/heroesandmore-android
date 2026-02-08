# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Native Android app for the HeroesAndMore collectibles marketplace. Built with Kotlin and Jetpack Compose, connects to the Django REST API.

## Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose with Material 3
- **Architecture**: MVVM + Clean Architecture
- **Networking**: Retrofit + OkHttp
- **DI**: Hilt (Dagger)
- **Auth**: JWT tokens in EncryptedSharedPreferences
- **Push**: Firebase Cloud Messaging
- **Payments**: Stripe Android SDK
- **Image Loading**: Coil
- **Camera**: CameraX

## Related Repositories

| Repo | URL |
|------|-----|
| Web (API) | https://github.com/nadermx/heroesandmore |
| Android | https://github.com/nadermx/heroesandmore-android (this repo) |
| iOS | https://github.com/nadermx/heroesandmore-ios |

## Team

| Name | Email |
|------|-------|
| John | john@nader.mx |
| Tony | tmgormond@gmail.com |
| Jim | jim@sickboys.com |

## Project Structure

```
app/src/main/java/com/heroesandmore/app/
├── data/                           # Data Layer
│   ├── api/                        # Retrofit interfaces (10 files)
│   │   ├── AuthApi.kt              # Login, register, profile, device tokens
│   │   ├── MarketplaceApi.kt       # Listings, bids, offers, orders (31 endpoints)
│   │   ├── CollectionsApi.kt       # Collections CRUD, valuation (16 endpoints)
│   │   ├── PricingApi.kt           # Price guide, grades, sales (8 endpoints)
│   │   ├── ItemsApi.kt             # Categories, search (5 endpoints)
│   │   ├── AlertsApi.kt            # Notifications, wishlists (13 endpoints)
│   │   ├── SellerApi.kt            # Dashboard, inventory (12 endpoints)
│   │   ├── SocialApi.kt            # Forums, messages, follows
│   │   ├── ScannerApi.kt           # Image recognition
│   │   └── AuthInterceptor.kt      # Bearer token injection, 401 handling
│   ├── dto/                        # Data Transfer Objects
│   │   ├── auth/AuthDtos.kt        # Login/register requests, token responses
│   │   ├── marketplace/MarketplaceDtos.kt  # Listing, bid, offer, order DTOs
│   │   ├── accounts/AccountDtos.kt # Profile, notification settings
│   │   ├── collections/            # Collection DTOs
│   │   ├── pricing/                # Price guide DTOs
│   │   ├── items/ItemDtos.kt       # Category, search DTOs
│   │   ├── alerts/AlertDtos.kt     # Notification, wishlist DTOs
│   │   ├── seller/SellerDtos.kt    # Dashboard, inventory DTOs
│   │   ├── social/SocialDtos.kt    # Forum, message DTOs
│   │   └── common/CommonDtos.kt    # PaginatedResponse<T>
│   ├── local/
│   │   └── TokenManager.kt         # EncryptedSharedPreferences token storage
│   └── repository/                 # Repository implementations (9 files)
│
├── domain/                         # Domain Layer
│   ├── model/                      # Domain models (9 files)
│   │   ├── User.kt                 # User, PublicProfile, AuthState
│   │   ├── Listing.kt              # Listing, Bid, Offer, Order (172 lines)
│   │   ├── Collection.kt           # Collection, CollectionItem, ValueHistory
│   │   ├── Category.kt             # Hierarchical categories
│   │   ├── PriceGuide.kt           # Price guide items, grades
│   │   ├── Alert.kt                # Notifications, wishlists
│   │   ├── Seller.kt               # Seller profile, subscription
│   │   ├── Social.kt               # Forums, messages
│   │   └── Scanner.kt              # Scan results
│   └── repository/                 # Repository interfaces (10 files)
│
├── presentation/                   # UI Layer
│   ├── screens/                    # Feature screens (7 groups, 18 files)
│   │   ├── auth/                   # LoginScreen, AuthViewModel
│   │   ├── home/                   # HomeScreen, HomeViewModel
│   │   ├── browse/                 # BrowseScreen, BrowseViewModel
│   │   ├── listing/                # ListingDetailScreen, CreateListingScreen + VMs
│   │   │                          # ListingDetailScreen includes FullscreenImageViewer
│   │   │                          # (pinch-to-zoom, double-tap, HorizontalPager)
│   │   ├── collections/            # CollectionsScreen, CollectionDetailScreen + VMs
│   │   ├── search/                 # SearchScreen, SearchViewModel
│   │   └── profile/                # ProfileScreen, MyOffersScreen + ViewModels
│   ├── components/                 # Reusable composables
│   │   ├── CommonComponents.kt     # Generic components
│   │   └── ListingCard.kt          # Listing preview card
│   │                                # ListingDetailScreen also has SellYoursCTA composable
│   ├── navigation/
│   │   ├── Screen.kt               # Sealed class with all routes
│   │   └── NavHost.kt              # Bottom nav + composable entries
│   └── theme/                      # Material 3 theming
│       ├── Color.kt
│       ├── Type.kt
│       └── Theme.kt
│
├── service/
│   └── FirebaseMessagingService.kt   # FCM push notifications
│
├── di/                             # Hilt modules
│   ├── AppModule.kt                # EncryptedSharedPrefs, TokenManager
│   ├── NetworkModule.kt            # OkHttp, Retrofit, API interfaces
│   └── RepositoryModule.kt         # Repository bindings
│
└── util/
    └── Resource.kt                 # Result wrapper (Success/Error/Loading)
```

## API Configuration

In `di/NetworkModule.kt`:
```kotlin
// Debug (Android emulator localhost)
private const val BASE_URL = "http://10.0.2.2:8000/api/v1/"

// Release
private const val BASE_URL = "https://heroesandmore.com/api/v1/"
```

## Building

```bash
# Debug APK
./gradlew assembleDebug

# Install on device
./gradlew installDebug

# Release build
./gradlew assembleRelease

# Run tests
./gradlew test

# Clean build
./gradlew clean
```

## Running Tests

```bash
./gradlew test                          # All unit tests
./gradlew test --tests="*AuthApiTest"   # Specific test class
./gradlew androidTest                   # Instrumented tests
./gradlew lint                          # Lint checks
```

Test files in `app/src/test/`:
- `AuthApiTest.kt` - Auth DTO serialization (26 test methods)
- `MarketplaceApiTest.kt` - Marketplace DTO tests
- `CollectionsApiTest.kt` - Collections DTO tests

## Key Patterns

### Repository Pattern with Resource Wrapper
```kotlin
sealed class Resource<T>(val data: T?, val message: String?) {
    class Success<T>(data: T) : Resource<T>(data, null)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data, null)
}

// Usage in ViewModel
viewModelScope.launch {
    when (val result = repository.getListings()) {
        is Resource.Success -> _uiState.update { it.copy(listings = result.data) }
        is Resource.Error -> _uiState.update { it.copy(error = result.message) }
        is Resource.Loading -> _uiState.update { it.copy(isLoading = true) }
    }
}
```

### ViewModel with StateFlow
```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MarketplaceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // Parallel loading
            launch { loadFeatured() }
            launch { loadEndingSoon() }
            launch { loadRecent() }
        }
    }
}

data class HomeUiState(
    val featuredListings: List<Listing> = emptyList(),
    val endingSoon: List<Listing> = emptyList(),
    val recentListings: List<Listing> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
```

### Token Management
```kotlin
@Singleton
class TokenManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    fun saveTokens(accessToken: String, refreshToken: String)
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun updateAccessToken(newToken: String)
    fun clearTokens()
    fun saveFcmToken(token: String)
    fun getFcmToken(): String?

    // StateFlow for login state
    val isLoggedInFlow: StateFlow<Boolean>
}
```

### Auth Interceptor
```kotlin
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Chain): Response {
        val request = chain.request()

        // Skip auth for login/register endpoints
        if (request.url.encodedPath.contains("/auth/token") ||
            request.url.encodedPath.contains("/register")) {
            return chain.proceed(request)
        }

        val token = tokenManager.getAccessToken()
        val authenticatedRequest = request.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(authenticatedRequest)
    }
}
```

## Navigation

All routes defined in `Screen.kt` sealed class:
```kotlin
sealed class Screen(val route: String) {
    // Main tabs
    object Home : Screen("home")
    object Browse : Screen("browse")
    object Sell : Screen("sell")
    object Collections : Screen("collections")
    object Profile : Screen("profile")

    // Parameterized routes
    object ListingDetail : Screen("listing/{listingId}") {
        fun createRoute(listingId: Int) = "listing/$listingId"
    }
    object CollectionDetail : Screen("collection/{collectionId}")
    // ... etc
}
```

## Firebase/FCM Setup

### Notification Channels (created in `HeroesAndMoreApp.kt`)
- `default_notification_channel_id` - General notifications
- `auction_alerts` - HIGH importance (bids, outbid, won)
- `messages` - HIGH importance (new messages)
- `price_alerts` - DEFAULT importance (price drops)

### Notification Types Handled
- `bid` - New bid on user's auction
- `outbid` - User was outbid
- `auction_won` - User won auction
- `offer_received` - New offer on listing
- `offer_accepted` - Offer was accepted
- `counter_offer` - Seller made counter-offer
- `order_shipped` - Order shipped
- `price_alert` - Price drop alert
- `wishlist_match` - Item matches wishlist
- `new_message` - New message

### Setup
1. Add `google-services.json` to `app/`
2. Configure FCM in Firebase Console
3. Device token registered on login via `AccountRepository.registerDeviceToken()`

## Dependencies

### Core
- androidx.core:core-ktx:1.12.0
- androidx.lifecycle:lifecycle-runtime-ktx:2.7.0
- androidx.activity:activity-compose:1.8.2

### Compose (BOM 2024.01.00)
- material3, icons-extended, ui, graphics, preview

### Navigation
- androidx.navigation:navigation-compose:2.7.6
- androidx.hilt:hilt-navigation-compose:1.1.0

### Hilt
- com.google.dagger:hilt-android:2.50

### Networking
- com.squareup.retrofit2:retrofit:2.9.0
- com.squareup.retrofit2:converter-gson:2.9.0
- com.squareup.okhttp3:okhttp:4.12.0
- com.squareup.okhttp3:logging-interceptor:4.12.0

### Storage
- androidx.security:security-crypto:1.1.0-alpha06
- androidx.datastore:datastore-preferences:1.0.0

### Media
- io.coil-kt:coil-compose:2.5.0
- androidx.camera (CameraX):1.3.1

### Payments
- com.stripe:stripe-android:20.37.0

### Firebase (BOM 32.7.0)
- firebase-messaging-ktx
- firebase-analytics-ktx

### Pagination
- androidx.paging:paging-runtime-ktx:3.2.1
- androidx.paging:paging-compose:3.2.1

## Build Configuration

```kotlin
android {
    compileSdk = 34

    defaultConfig {
        minSdk = 26
        targetSdk = 34
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlinOptions {
        jvmTarget = "21"
    }
}
```

## Common Issues

**Build fails**: Run `./gradlew clean` then rebuild.

**Auth issues**: Check TokenManager in EncryptedSharedPreferences. Clear app data to reset.

**Missing google-services.json**: Download from Firebase Console.

**Emulator can't reach localhost**: Use `10.0.2.2` instead of `localhost` for API URL.

**ProGuard issues**: Check `proguard-rules.pro` for missing keep rules.

## Debugging

```bash
# View logs
adb logcat | grep -i heroesandmore

# Filter by tag
adb logcat -s HeroesAndMore

# Check dependencies
./gradlew dependencies

# Dependency tree for specific configuration
./gradlew app:dependencies --configuration releaseRuntimeClasspath
```

## Safe API Call Pattern

Repository implementations use a `safeApiCall` helper for consistent error handling:
```kotlin
private suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> {
    return try {
        Resource.Success(apiCall())
    } catch (e: HttpException) {
        Resource.Error(e.message() ?: "HTTP error")
    } catch (e: IOException) {
        Resource.Error("Network error")
    }
}
```

## Security

- JWT tokens stored in EncryptedSharedPreferences (AES256-GCM)
- Bearer token added to all API requests via AuthInterceptor
- Token refresh logic handles 401 responses
- Sensitive keys excluded from git via `.gitignore`
