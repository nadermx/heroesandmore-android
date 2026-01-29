package com.heroesandmore.app.presentation.screens.listing

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.heroesandmore.app.domain.model.ListingDetail
import com.heroesandmore.app.domain.model.ListingType
import com.heroesandmore.app.presentation.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingDetailScreen(
    listingId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToSeller: (String) -> Unit,
    onNavigateToCheckout: (Int) -> Unit,
    viewModel: ListingDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(listingId) {
        viewModel.loadListing(listingId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Listing") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleSaved() }) {
                        Icon(
                            if (uiState.listing?.isSaved == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (uiState.listing?.isSaved == true) "Unsave" else "Save"
                        )
                    }
                    IconButton(onClick = { /* Share */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }
            )
        },
        bottomBar = {
            uiState.listing?.let { listing ->
                ListingBottomBar(
                    listing = listing,
                    onBuyNow = { onNavigateToCheckout(listing.id) },
                    onPlaceBid = { viewModel.showBidDialog() },
                    onMakeOffer = { viewModel.showOfferDialog() }
                )
            }
        }
    ) { padding ->
        when {
            uiState.isLoading -> {
                LoadingIndicator(modifier = Modifier.padding(padding))
            }
            uiState.error != null -> {
                ErrorMessage(
                    message = uiState.error!!,
                    onRetry = { viewModel.loadListing(listingId) },
                    modifier = Modifier.padding(padding)
                )
            }
            uiState.listing != null -> {
                ListingDetailContent(
                    listing = uiState.listing!!,
                    onSellerClick = { onNavigateToSeller(uiState.listing!!.seller.username) },
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }

    // Bid Dialog
    if (uiState.showBidDialog) {
        BidDialog(
            currentPrice = uiState.listing?.currentPrice ?: "0",
            onDismiss = { viewModel.hideBidDialog() },
            onSubmit = { amount -> viewModel.placeBid(amount) }
        )
    }

    // Offer Dialog
    if (uiState.showOfferDialog) {
        OfferDialog(
            listingPrice = uiState.listing?.price ?: "0",
            onDismiss = { viewModel.hideOfferDialog() },
            onSubmit = { amount, message -> viewModel.makeOffer(amount, message) }
        )
    }
}

@Composable
private fun ListingDetailContent(
    listing: ListingDetail,
    onSellerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Image Gallery
        item {
            ImageGallery(images = listing.images.map { it.url })
        }

        // Title and Price
        item {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = listing.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (listing.listingType == ListingType.AUCTION) {
                        Column {
                            Text(
                                text = "Current Bid",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "$${listing.currentPrice}",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "${listing.bidCount} bids",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        Text(
                            text = "$${listing.price}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Column(horizontalAlignment = Alignment.End) {
                        ConditionChip(condition = listing.condition)
                        if (listing.grade != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            GradeBadge(
                                grade = listing.grade,
                                gradingCompany = listing.gradingCompany
                            )
                        }
                    }
                }
            }
        }

        // Seller Info
        item {
            SellerCard(
                seller = listing.seller,
                onClick = onSellerClick
            )
        }

        // Description
        if (!listing.description.isNullOrEmpty()) {
            item {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Description",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = listing.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // Shipping
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.LocalShipping,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Shipping",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = if (listing.shippingPrice == null || listing.shippingPrice == "0.00") {
                                "Free Shipping"
                            } else {
                                "$${listing.shippingPrice}"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Recent Sales
        if (listing.recentSales.isNotEmpty()) {
            item {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Recent Sales",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            items(listing.recentSales.take(5)) { sale ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = sale.source,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$${sale.price}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = sale.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun ImageGallery(
    images: List<String>,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { images.size.coerceAtLeast(1) })

    Box(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) { page ->
            if (images.isNotEmpty()) {
                AsyncImage(
                    model = images[page],
                    contentDescription = "Image ${page + 1}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }

        if (images.size > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(images.size) { index ->
                    val color = if (pagerState.currentPage == index) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    }
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .size(8.dp)
                            .padding(1.dp)
                    ) {
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = color,
                            modifier = Modifier.fillMaxSize()
                        ) {}
                    }
                }
            }
        }
    }
}

@Composable
private fun SellerCard(
    seller: com.heroesandmore.app.domain.model.PublicProfile,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = seller.avatarUrl,
                contentDescription = seller.username,
                modifier = Modifier.size(48.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = seller.username,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )
                    if (seller.isSellerVerified) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            Icons.Default.Verified,
                            contentDescription = "Verified",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                seller.rating?.let { rating ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "%.1f".format(rating),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ListingBottomBar(
    listing: ListingDetail,
    onBuyNow: () -> Unit,
    onPlaceBid: () -> Unit,
    onMakeOffer: () -> Unit
) {
    Surface(
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (listing.listingType == ListingType.AUCTION) {
                Button(
                    onClick = onPlaceBid,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Place Bid")
                }
            } else {
                if (listing.allowOffers) {
                    OutlinedButton(
                        onClick = onMakeOffer,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Make Offer")
                    }
                }
                Button(
                    onClick = onBuyNow,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Buy Now")
                }
            }
        }
    }
}

@Composable
private fun BidDialog(
    currentPrice: String,
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit
) {
    var bidAmount by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Place Bid") },
        text = {
            Column {
                Text("Current bid: $$currentPrice")
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = bidAmount,
                    onValueChange = { bidAmount = it },
                    label = { Text("Your bid") },
                    prefix = { Text("$") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(bidAmount) },
                enabled = bidAmount.isNotEmpty()
            ) {
                Text("Place Bid")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun OfferDialog(
    listingPrice: String,
    onDismiss: () -> Unit,
    onSubmit: (String, String?) -> Unit
) {
    var offerAmount by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Make Offer") },
        text = {
            Column {
                Text("Asking price: $$listingPrice")
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = offerAmount,
                    onValueChange = { offerAmount = it },
                    label = { Text("Your offer") },
                    prefix = { Text("$") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Message (optional)") },
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(offerAmount, message.ifEmpty { null }) },
                enabled = offerAmount.isNotEmpty()
            ) {
                Text("Send Offer")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
