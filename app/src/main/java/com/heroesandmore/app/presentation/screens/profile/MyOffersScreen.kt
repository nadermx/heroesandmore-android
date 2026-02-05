package com.heroesandmore.app.presentation.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.heroesandmore.app.domain.model.Offer
import com.heroesandmore.app.domain.model.OfferStatus
import com.heroesandmore.app.presentation.components.ErrorView
import com.heroesandmore.app.presentation.components.LoadingView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyOffersScreen(
    onNavigateBack: () -> Unit,
    onNavigateToListing: (Int) -> Unit,
    onNavigateToCheckout: (Int) -> Unit,
    viewModel: MyOffersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showCounterDialog by remember { mutableStateOf<Offer?>(null) }
    var counterAmount by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }

    // Show success message
    LaunchedEffect(uiState.actionSuccess) {
        uiState.actionSuccess?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSuccess()
        }
    }

    // Show error message
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Offers") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        when {
            uiState.isLoading -> {
                LoadingView()
            }
            uiState.offers.isEmpty() -> {
                EmptyOffersView(modifier = Modifier.padding(padding))
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.offers) { offer ->
                        OfferCard(
                            offer = offer,
                            isActionInProgress = uiState.actionInProgress == offer.id,
                            onAccept = { viewModel.acceptOffer(offer.id) },
                            onDecline = { viewModel.declineOffer(offer.id) },
                            onCounter = {
                                counterAmount = ""
                                showCounterDialog = offer
                            },
                            onAcceptCounter = { viewModel.acceptCounterOffer(offer.id) },
                            onDeclineCounter = { viewModel.declineCounterOffer(offer.id) },
                            onViewListing = { onNavigateToListing(offer.listing.id) }
                        )
                    }
                }
            }
        }
    }

    // Counter offer dialog
    showCounterDialog?.let { offer ->
        AlertDialog(
            onDismissRequest = { showCounterDialog = null },
            title = { Text("Make Counter Offer") },
            text = {
                Column {
                    Text(
                        "Their offer: $${offer.amount}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = counterAmount,
                        onValueChange = { counterAmount = it },
                        label = { Text("Your counter offer") },
                        prefix = { Text("$") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.counterOffer(offer.id, counterAmount)
                        showCounterDialog = null
                    },
                    enabled = counterAmount.isNotBlank()
                ) {
                    Text("Send")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCounterDialog = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun EmptyOffersView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Outlined.LocalOffer,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "No Offers",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            "Offers you make or receive will appear here",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun OfferCard(
    offer: Offer,
    isActionInProgress: Boolean,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    onCounter: () -> Unit,
    onAcceptCounter: () -> Unit,
    onDeclineCounter: () -> Unit,
    onViewListing: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Listing info row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Listing image
                AsyncImage(
                    model = offer.listing.imageUrl,
                    contentDescription = offer.listing.title,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(MaterialTheme.shapes.small),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Listing details
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = offer.listing.title,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Text(
                            "List: $${offer.listing.price}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Offer: $${offer.amount}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                            )
                        )
                    }
                    // Show counter amount if present
                    offer.counterAmount?.let { counter ->
                        Text(
                            "Counter: $$counter",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Status badge
                StatusBadge(status = offer.status)
            }

            // Time remaining for countered offers
            if (offer.status == OfferStatus.COUNTERED && offer.timeRemaining != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Expires in ${offer.timeRemaining}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Action buttons based on status and role
            if (offer.status == OfferStatus.PENDING && !offer.isFromBuyer) {
                // Seller actions for pending offers
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onAccept,
                        enabled = !isActionInProgress,
                        modifier = Modifier.weight(1f)
                    ) {
                        if (isActionInProgress) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Accept")
                        }
                    }
                    OutlinedButton(
                        onClick = onCounter,
                        enabled = !isActionInProgress,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Counter")
                    }
                    OutlinedButton(
                        onClick = onDecline,
                        enabled = !isActionInProgress,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Decline")
                    }
                }
            }

            if (offer.status == OfferStatus.COUNTERED && offer.isFromBuyer) {
                // Buyer actions for counter-offers
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onAcceptCounter,
                        enabled = !isActionInProgress,
                        modifier = Modifier.weight(1f)
                    ) {
                        if (isActionInProgress) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Accept Counter")
                        }
                    }
                    OutlinedButton(
                        onClick = onDeclineCounter,
                        enabled = !isActionInProgress,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Decline")
                    }
                }
            }

            // Message if present
            offer.message?.takeIf { it.isNotBlank() }?.let { message ->
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(status: OfferStatus) {
    val (color, text) = when (status) {
        OfferStatus.ACCEPTED -> MaterialTheme.colorScheme.primary to "Accepted"
        OfferStatus.DECLINED -> MaterialTheme.colorScheme.error to "Declined"
        OfferStatus.COUNTERED -> MaterialTheme.colorScheme.tertiary to "Countered"
        OfferStatus.EXPIRED -> MaterialTheme.colorScheme.onSurfaceVariant to "Expired"
        OfferStatus.PENDING -> MaterialTheme.colorScheme.secondary to "Pending"
    }

    Surface(
        color = color.copy(alpha = 0.15f),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
