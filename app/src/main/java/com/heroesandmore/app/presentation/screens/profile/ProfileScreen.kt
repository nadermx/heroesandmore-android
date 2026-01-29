package com.heroesandmore.app.presentation.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.heroesandmore.app.presentation.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToMyListings: () -> Unit,
    onNavigateToMyOrders: () -> Unit,
    onNavigateToSavedListings: () -> Unit,
    onNavigateToMessages: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToWishlists: () -> Unit,
    onNavigateToPriceAlerts: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                actions = {
                    if (uiState.isLoggedIn) {
                        IconButton(onClick = onNavigateToSettings) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (!uiState.isLoggedIn) {
            // Not logged in
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Sign in to access your profile",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Manage collections, track orders, and more",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onNavigateToLogin,
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text("Sign In")
                }
            }
        } else {
            // Logged in
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Profile Header
                item {
                    ProfileHeader(
                        username = uiState.user?.username ?: "",
                        avatarUrl = uiState.user?.avatarUrl,
                        rating = uiState.user?.rating,
                        isVerified = uiState.user?.isSellerVerified ?: false
                    )
                }

                // Menu Items
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    ProfileMenuItem(
                        icon = Icons.Default.Inventory2,
                        title = "My Listings",
                        subtitle = "Manage your listings",
                        onClick = onNavigateToMyListings
                    )
                }

                item {
                    ProfileMenuItem(
                        icon = Icons.Default.ShoppingBag,
                        title = "My Orders",
                        subtitle = "Track your purchases",
                        onClick = onNavigateToMyOrders
                    )
                }

                item {
                    ProfileMenuItem(
                        icon = Icons.Default.Favorite,
                        title = "Saved Listings",
                        subtitle = "Items you've saved",
                        onClick = onNavigateToSavedListings
                    )
                }

                item {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }

                item {
                    ProfileMenuItem(
                        icon = Icons.Default.Message,
                        title = "Messages",
                        subtitle = "Chat with buyers and sellers",
                        onClick = onNavigateToMessages,
                        badge = uiState.unreadMessages
                    )
                }

                item {
                    ProfileMenuItem(
                        icon = Icons.Default.Notifications,
                        title = "Notifications",
                        subtitle = "Alerts and updates",
                        onClick = onNavigateToNotifications,
                        badge = uiState.unreadNotifications
                    )
                }

                item {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }

                item {
                    ProfileMenuItem(
                        icon = Icons.Default.List,
                        title = "Wishlists",
                        subtitle = "Items you want to find",
                        onClick = onNavigateToWishlists
                    )
                }

                item {
                    ProfileMenuItem(
                        icon = Icons.Default.PriceChange,
                        title = "Price Alerts",
                        subtitle = "Get notified on price changes",
                        onClick = onNavigateToPriceAlerts
                    )
                }

                item {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }

                item {
                    ProfileMenuItem(
                        icon = Icons.Default.Logout,
                        title = "Sign Out",
                        subtitle = null,
                        onClick = { viewModel.logout() },
                        tint = MaterialTheme.colorScheme.error
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
private fun ProfileHeader(
    username: String,
    avatarUrl: String?,
    rating: Double?,
    isVerified: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = avatarUrl,
            contentDescription = username,
            modifier = Modifier.size(80.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = username,
                style = MaterialTheme.typography.headlineSmall
            )
            if (isVerified) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    Icons.Default.Verified,
                    contentDescription = "Verified",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        if (rating != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "%.1f rating".format(rating),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ProfileMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String?,
    onClick: () -> Unit,
    badge: Int? = null,
    tint: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    ListItem(
        headlineContent = {
            Text(title, color = tint)
        },
        supportingContent = subtitle?.let {
            { Text(it) }
        },
        leadingContent = {
            Icon(icon, contentDescription = null, tint = tint)
        },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (badge != null && badge > 0) {
                    Badge {
                        Text(if (badge > 99) "99+" else badge.toString())
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Composable
private fun Modifier.clickable(onClick: () -> Unit): Modifier {
    return this.then(
        androidx.compose.foundation.clickable(onClick = onClick)
    )
}
