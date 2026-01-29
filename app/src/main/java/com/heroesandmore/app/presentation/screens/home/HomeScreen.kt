package com.heroesandmore.app.presentation.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.heroesandmore.app.domain.model.Listing
import com.heroesandmore.app.presentation.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToListing: (Int) -> Unit,
    onNavigateToCategory: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Heroes & More") },
                actions = {
                    IconButton(onClick = onNavigateToSearch) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
        }
    ) { padding ->
        when {
            uiState.isLoading && uiState.featuredListings.isEmpty() -> {
                LoadingIndicator(modifier = Modifier.padding(padding))
            }
            uiState.error != null && uiState.featuredListings.isEmpty() -> {
                ErrorMessage(
                    message = uiState.error!!,
                    onRetry = { viewModel.refresh() },
                    modifier = Modifier.padding(padding)
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    // Featured Listings
                    item {
                        SectionHeader(
                            title = "Featured",
                            actionLabel = "See All",
                            onAction = { onNavigateToCategory("featured") }
                        )
                    }

                    item {
                        FeaturedListingsRow(
                            listings = uiState.featuredListings,
                            onListingClick = onNavigateToListing
                        )
                    }

                    // Ending Soon
                    if (uiState.endingSoon.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                            SectionHeader(
                                title = "Ending Soon",
                                actionLabel = "See All",
                                onAction = { onNavigateToCategory("ending-soon") }
                            )
                        }

                        item {
                            EndingSoonRow(
                                listings = uiState.endingSoon,
                                onListingClick = onNavigateToListing
                            )
                        }
                    }

                    // Recently Listed
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        SectionHeader(
                            title = "Recently Listed",
                            actionLabel = "See All",
                            onAction = { onNavigateToCategory("recent") }
                        )
                    }

                    items(
                        items = uiState.recentListings,
                        key = { it.id }
                    ) { listing ->
                        ListingCardCompact(
                            listing = listing,
                            onClick = { onNavigateToListing(listing.id) },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FeaturedListingsRow(
    listings: List<Listing>,
    onListingClick: (Int) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = listings,
            key = { it.id }
        ) { listing ->
            ListingCard(
                listing = listing,
                onClick = { onListingClick(listing.id) },
                modifier = Modifier.width(180.dp)
            )
        }
    }
}

@Composable
private fun EndingSoonRow(
    listings: List<Listing>,
    onListingClick: (Int) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = listings,
            key = { it.id }
        ) { listing ->
            ListingCard(
                listing = listing,
                onClick = { onListingClick(listing.id) },
                modifier = Modifier.width(160.dp)
            )
        }
    }
}
