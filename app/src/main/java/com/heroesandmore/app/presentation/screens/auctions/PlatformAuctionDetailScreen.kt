package com.heroesandmore.app.presentation.screens.auctions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.heroesandmore.app.domain.model.AuctionEvent
import com.heroesandmore.app.domain.model.AuctionLotSubmission
import com.heroesandmore.app.presentation.components.EmptyState
import com.heroesandmore.app.presentation.components.ErrorMessage
import com.heroesandmore.app.presentation.components.ListingCard
import com.heroesandmore.app.presentation.components.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlatformAuctionDetailScreen(
    slug: String,
    onNavigateBack: () -> Unit,
    onNavigateToListing: (Int) -> Unit,
    onNavigateToSubmitLot: (String) -> Unit,
    viewModel: PlatformAuctionDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.event?.title ?: "Auction Event",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            if (uiState.event?.acceptingSubmissions == true) {
                ExtendedFloatingActionButton(
                    onClick = {
                        uiState.event?.slug?.let { onNavigateToSubmitLot(it) }
                    },
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("Submit a Lot") },
                    containerColor = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) { padding ->
        when {
            uiState.isLoading && uiState.event == null -> {
                LoadingIndicator(modifier = Modifier.padding(padding))
            }
            uiState.error != null && uiState.event == null -> {
                ErrorMessage(
                    message = uiState.error!!,
                    onRetry = { viewModel.refresh() },
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
                    // Event header
                    item(span = { GridItemSpan(2) }) {
                        EventHeader(event = uiState.event)
                    }

                    // My submissions section
                    if (uiState.mySubmissions.isNotEmpty()) {
                        item(span = { GridItemSpan(2) }) {
                            MySubmissionsSection(submissions = uiState.mySubmissions)
                        }
                    }

                    // Lots section header
                    item(span = { GridItemSpan(2) }) {
                        Text(
                            text = "Lots (${uiState.lots.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    if (uiState.lots.isEmpty()) {
                        item(span = { GridItemSpan(2) }) {
                            Text(
                                text = "No lots have been added to this event yet.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                        }
                    } else {
                        items(
                            items = uiState.lots,
                            key = { it.id }
                        ) { listing ->
                            ListingCard(
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

@Composable
private fun EventHeader(event: AuctionEvent?) {
    if (event == null) return

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (event.description != null) {
            Text(
                text = event.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Status chip
            val (statusColor, statusLabel) = when (event.status.lowercase()) {
                "live" -> Color(0xFFE53935) to "Live"
                "preview" -> Color(0xFFFFA000) to "Preview"
                "ended" -> MaterialTheme.colorScheme.onSurfaceVariant to "Ended"
                else -> MaterialTheme.colorScheme.outline to event.status.replaceFirstChar { it.uppercase() }
            }

            Surface(
                shape = RoundedCornerShape(4.dp),
                color = statusColor.copy(alpha = 0.15f)
            ) {
                Text(
                    text = statusLabel,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = statusColor
                )
            }

            if (event.isLive) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFE53935)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.FiberManualRecord,
                            contentDescription = null,
                            modifier = Modifier.size(10.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "LIVE",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            if (event.acceptingSubmissions) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFF4CAF50)
                ) {
                    Text(
                        text = "Accepting Submissions",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Gavel,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${event.lotCount} lots",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (event.cadence != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = event.cadence.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        if (event.submissionDeadline != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Timer,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFFFFA000)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Submission deadline: ${event.submissionDeadline}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFFFA000)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
    }
}

@Composable
private fun MySubmissionsSection(submissions: List<AuctionLotSubmission>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "My Submissions",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        submissions.forEach { submission ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = submission.listingTitle,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Submitted: ${submission.submittedAt}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    SubmissionStatusBadge(status = submission.status)
                }

                if (submission.staffNotes != null) {
                    Text(
                        text = "Note: ${submission.staffNotes}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                    )
                }
            }
        }

        HorizontalDivider(modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
private fun SubmissionStatusBadge(status: String) {
    val (color, label) = when (status.lowercase()) {
        "pending" -> Color(0xFFFFA000) to "Pending"
        "approved" -> Color(0xFF4CAF50) to "Approved"
        "rejected" -> Color(0xFFE53935) to "Rejected"
        "withdrawn" -> MaterialTheme.colorScheme.onSurfaceVariant to "Withdrawn"
        else -> MaterialTheme.colorScheme.outline to status.replaceFirstChar { it.uppercase() }
    }

    Surface(
        shape = RoundedCornerShape(4.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
    }
}
