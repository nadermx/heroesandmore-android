package com.heroesandmore.app.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.heroesandmore.app.MainActivity
import com.heroesandmore.app.R
import com.heroesandmore.app.data.local.TokenManager
import com.heroesandmore.app.domain.repository.AccountRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HeroesFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    lateinit var accountRepository: AccountRepository

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        // Save token locally
        tokenManager.saveFcmToken(token)

        // Register with server if logged in
        if (tokenManager.getAccessToken() != null) {
            serviceScope.launch {
                accountRepository.registerDeviceToken(token)
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Handle notification payload
        remoteMessage.notification?.let { notification ->
            showNotification(
                title = notification.title ?: "Heroes & More",
                body = notification.body ?: "",
                data = remoteMessage.data
            )
        }

        // Handle data payload
        if (remoteMessage.data.isNotEmpty()) {
            handleDataMessage(remoteMessage.data)
        }
    }

    private fun showNotification(
        title: String,
        body: String,
        data: Map<String, String>
    ) {
        val channelId = getChannelIdForType(data["type"])

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            // Pass deep link data
            data["listing_id"]?.let { putExtra("listing_id", it.toIntOrNull()) }
            data["order_id"]?.let { putExtra("order_id", it.toIntOrNull()) }
            data["thread_id"]?.let { putExtra("thread_id", it.toIntOrNull()) }
            data["user_id"]?.let { putExtra("user_id", it.toIntOrNull()) }
            data["type"]?.let { putExtra("notification_type", it) }
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Create channel for Android O+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                getChannelNameForType(data["type"]),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun handleDataMessage(data: Map<String, String>) {
        when (data["type"]) {
            "bid" -> handleBidNotification(data)
            "outbid" -> handleOutbidNotification(data)
            "auction_won" -> handleAuctionWonNotification(data)
            "offer_received" -> handleOfferNotification(data)
            "offer_accepted" -> handleOfferAcceptedNotification(data)
            "order_shipped" -> handleShippingNotification(data)
            "price_alert" -> handlePriceAlertNotification(data)
            "wishlist_match" -> handleWishlistMatchNotification(data)
            "new_message" -> handleMessageNotification(data)
        }
    }

    private fun handleBidNotification(data: Map<String, String>) {
        // Handle new bid on user's auction
    }

    private fun handleOutbidNotification(data: Map<String, String>) {
        // Handle being outbid
    }

    private fun handleAuctionWonNotification(data: Map<String, String>) {
        // Handle winning an auction
    }

    private fun handleOfferNotification(data: Map<String, String>) {
        // Handle new offer received
    }

    private fun handleOfferAcceptedNotification(data: Map<String, String>) {
        // Handle offer accepted
    }

    private fun handleShippingNotification(data: Map<String, String>) {
        // Handle order shipped
    }

    private fun handlePriceAlertNotification(data: Map<String, String>) {
        // Handle price alert triggered
    }

    private fun handleWishlistMatchNotification(data: Map<String, String>) {
        // Handle wishlist match found
    }

    private fun handleMessageNotification(data: Map<String, String>) {
        // Handle new message
    }

    private fun getChannelIdForType(type: String?): String {
        return when (type) {
            "bid", "outbid", "auction_won" -> "auctions"
            "offer_received", "offer_accepted" -> "offers"
            "order_shipped" -> "orders"
            "price_alert", "wishlist_match" -> "alerts"
            "new_message" -> "messages"
            else -> "general"
        }
    }

    private fun getChannelNameForType(type: String?): String {
        return when (type) {
            "bid", "outbid", "auction_won" -> "Auctions"
            "offer_received", "offer_accepted" -> "Offers"
            "order_shipped" -> "Orders"
            "price_alert", "wishlist_match" -> "Alerts"
            "new_message" -> "Messages"
            else -> "General"
        }
    }
}
