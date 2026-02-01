# CLAUDE.md

This file provides guidance to Claude Code when working with the HeroesAndMore Android app.

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
├── data/                    # Data layer
│   ├── api/                 # Retrofit interfaces
│   ├── dto/                 # Data Transfer Objects
│   ├── local/               # Room DB, TokenManager
│   └── repository/          # Repository implementations
├── domain/                  # Domain layer
│   ├── model/               # Domain models
│   └── repository/          # Repository interfaces
├── presentation/            # UI layer
│   ├── theme/               # Compose theme
│   ├── navigation/          # Nav graph
│   ├── components/          # Reusable composables
│   └── screens/             # Screens + ViewModels
├── di/                      # Hilt modules
└── service/                 # FCM service
```

## API Configuration

In `di/NetworkModule.kt`:
```kotlin
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
```

## Common Commands

```bash
./gradlew clean              # Clean build
./gradlew dependencies       # Check deps
./gradlew lint               # Run linter
adb logcat                   # View logs
```

## Firebase Setup

1. Add `google-services.json` to `app/`
2. Configure FCM in Firebase Console
3. Backend sends push via FCM API

## Common Issues

**Build fails**: Run `./gradlew clean` then rebuild.

**Auth issues**: Check TokenManager in EncryptedSharedPreferences.

**Missing google-services.json**: Download from Firebase Console.
