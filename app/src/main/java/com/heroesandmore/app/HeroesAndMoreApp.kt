package com.heroesandmore.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HeroesAndMoreApp : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java)

            // Main notification channel
            val mainChannel = NotificationChannel(
                getString(R.string.default_notification_channel_id),
                getString(R.string.default_notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "General notifications from HeroesAndMore"
            }

            // Auction alerts channel
            val auctionChannel = NotificationChannel(
                "auction_alerts",
                "Auction Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alerts for auctions ending soon and outbid notifications"
            }

            // Messages channel
            val messagesChannel = NotificationChannel(
                "messages",
                "Messages",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "New message notifications"
            }

            // Price alerts channel
            val priceChannel = NotificationChannel(
                "price_alerts",
                "Price Alerts",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Price drop and wishlist match notifications"
            }

            notificationManager.createNotificationChannels(
                listOf(mainChannel, auctionChannel, messagesChannel, priceChannel)
            )
        }
    }
}
