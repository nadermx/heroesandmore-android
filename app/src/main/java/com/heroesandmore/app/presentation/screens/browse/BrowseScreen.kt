package com.heroesandmore.app.presentation.screens.browse

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.heroesandmore.app.domain.model.Category
import com.heroesandmore.app.presentation.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseScreen(
    onNavigateToCategory: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
    viewModel: BrowseViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Browse") },
                actions = {
                    IconButton(onClick = onNavigateToSearch) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> {
                LoadingIndicator(modifier = Modifier.padding(padding))
            }
            uiState.error != null -> {
                ErrorMessage(
                    message = uiState.error!!,
                    onRetry = { viewModel.loadCategories() },
                    modifier = Modifier.padding(padding)
                )
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = uiState.categories,
                        key = { it.id }
                    ) { category ->
                        CategoryCard(
                            category = category,
                            onClick = { onNavigateToCategory(category.slug) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: Category,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = getCategoryIcon(category.slug),
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = category.name,
                style = MaterialTheme.typography.titleSmall
            )

            category.listingCount?.let { count ->
                Text(
                    text = "$count items",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun getCategoryIcon(slug: String): androidx.compose.ui.graphics.vector.ImageVector {
    return when {
        slug.contains("card") -> Icons.Default.Style
        slug.contains("comic") -> Icons.Default.MenuBook
        slug.contains("figure") || slug.contains("toy") -> Icons.Default.SmartToy
        slug.contains("game") -> Icons.Default.SportsEsports
        slug.contains("coin") -> Icons.Default.Paid
        slug.contains("stamp") -> Icons.Default.LocalPostOffice
        slug.contains("auto") -> Icons.Default.DirectionsCar
        slug.contains("art") -> Icons.Default.Palette
        else -> Icons.Default.Category
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListingsScreen(
    categorySlug: String,
    onNavigateBack: () -> Unit,
    onNavigateToListing: (Int) -> Unit,
    viewModel: CategoryListingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(categorySlug) {
        viewModel.loadCategory(categorySlug)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.categoryName ?: "Category") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleViewMode() }) {
                        Icon(
                            if (uiState.isGridView) Icons.Default.ViewList else Icons.Default.GridView,
                            contentDescription = "Toggle view"
                        )
                    }
                }
            )
        }
    ) { padding ->
        when {
            uiState.isLoading && uiState.listings.isEmpty() -> {
                LoadingIndicator(modifier = Modifier.padding(padding))
            }
            uiState.error != null && uiState.listings.isEmpty() -> {
                ErrorMessage(
                    message = uiState.error!!,
                    onRetry = { viewModel.loadCategory(categorySlug) },
                    modifier = Modifier.padding(padding)
                )
            }
            uiState.listings.isEmpty() -> {
                EmptyState(
                    icon = Icons.Default.Inventory2,
                    title = "No listings found",
                    description = "There are no listings in this category yet.",
                    modifier = Modifier.padding(padding)
                )
            }
            else -> {
                if (uiState.isGridView) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = uiState.listings,
                            key = { it.id }
                        ) { listing ->
                            ListingCard(
                                listing = listing,
                                onClick = { onNavigateToListing(listing.id) }
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = uiState.listings,
                            key = { it.id }
                        ) { listing ->
                            ListingCardCompact(
                                listing = listing,
                                onClick = { onNavigateToListing(listing.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
