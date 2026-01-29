# HeroesAndMore Android App

## Overview

Native Android app for the HeroesAndMore collectibles marketplace. Built with Kotlin, Jetpack Compose, and follows MVVM + Clean Architecture patterns.

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material 3
- **Architecture**: MVVM + Clean Architecture
- **Networking**: Retrofit + OkHttp
- **Image Loading**: Coil
- **Local Storage**: Room (SQLite) + EncryptedSharedPreferences
- **DI**: Hilt (Dagger)
- **Navigation**: Jetpack Navigation Compose
- **Push Notifications**: Firebase Cloud Messaging (FCM)
- **Camera**: CameraX (for scanner)
- **Payments**: Stripe Android SDK

## Project Structure

```
app/src/main/java/com/heroesandmore/app/
├── HeroesAndMoreApp.kt          # Application class
├── MainActivity.kt              # Main activity
├── data/                        # Data Layer
│   ├── api/                     # Retrofit API interfaces
│   ├── dto/                     # Data Transfer Objects
│   ├── local/                   # Room DB + TokenManager
│   └── repository/              # Repository implementations
├── domain/                      # Domain Layer
│   ├── model/                   # Domain models
│   └── repository/              # Repository interfaces
├── presentation/                # Presentation Layer
│   ├── theme/                   # Compose theme
│   ├── navigation/              # Navigation graph
│   ├── components/              # Reusable composables
│   └── screens/                 # Screen composables + ViewModels
├── di/                          # Hilt modules
├── service/                     # Firebase messaging service
└── util/                        # Utilities (Resource class)
```

## API Configuration

The app connects to the Django REST API at `https://heroesandmore.com/api/v1/`.

API configuration is in `di/NetworkModule.kt`:
- Base URL: `https://heroesandmore.com/api/v1/`
- Auth: JWT Bearer tokens
- Token refresh handled automatically by AuthInterceptor

## Building

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Run tests
./gradlew test
```

## Key Files

### API Interfaces (data/api/)
- `AuthApi.kt` - Authentication endpoints
- `MarketplaceApi.kt` - Listings, bids, offers, orders
- `CollectionsApi.kt` - Collection management
- `PricingApi.kt` - Price guide
- `AlertsApi.kt` - Notifications, wishlists, price alerts
- `SocialApi.kt` - Following, messages, forums
- `ScannerApi.kt` - Image scanning
- `SellerApi.kt` - Seller dashboard, inventory
- `ItemsApi.kt` - Categories, search

### DTOs (data/dto/)
Organized by feature:
- `auth/` - Login, register, tokens
- `accounts/` - Profile, settings
- `marketplace/` - Listings, orders, reviews
- `collections/` - Collections, items
- `pricing/` - Price guide items, sales
- `alerts/` - Notifications, wishlists
- `social/` - Messages, forums, follows
- `scanner/` - Scan results
- `seller/` - Dashboard, inventory
- `items/` - Categories

### Screens (presentation/screens/)
- `auth/` - Login, Register
- `home/` - Home feed
- `browse/` - Category browsing
- `listing/` - Listing detail
- `collections/` - Collection management
- `profile/` - User profile
- (More to be added)

## Feature Parity with Website

| Feature | Status |
|---------|--------|
| Browse listings | ✓ |
| Search | ✓ |
| Listing detail | ✓ |
| Create listing | ✓ |
| Image upload | ✓ |
| Place bid | ✓ |
| Make offer | ✓ |
| Buy/checkout | ✓ |
| Collections | ✓ |
| Price guide | ✓ |
| Scanner | ✓ |
| Messages | ✓ |
| Forums | ✓ |
| Push notifications | ✓ |
| Profile | ✓ |
| Seller dashboard | ✓ |

## Firebase Setup

1. Create a Firebase project
2. Add Android app with package `com.heroesandmore.app`
3. Download `google-services.json` to `app/`
4. Enable Cloud Messaging

## Stripe Setup

1. Add Stripe publishable key to `strings.xml` or `BuildConfig`
2. Server provides payment intents via API
3. Use Stripe SDK for card input

## Testing

```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest
```

## Release Checklist

1. Update version in `app/build.gradle.kts`
2. Ensure `google-services.json` is present
3. Configure signing key
4. Build release APK/Bundle
5. Test on physical devices
6. Upload to Play Store

## Common Commands

```bash
# Clean build
./gradlew clean

# Generate APK
./gradlew assembleDebug

# Install on device
./gradlew installDebug

# Check dependencies
./gradlew dependencies
```
