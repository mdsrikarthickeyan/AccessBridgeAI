package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.BarrierCategory
import com.example.model.BarrierReport
import com.example.model.BarrierSeverity
import com.example.model.BarrierStatus
import com.example.ui.components.AccessibleButton
import com.example.ui.components.AccessibleConfirmationDialog
import com.example.ui.components.AccessibleTextField
import com.example.ui.components.BarrierProgressTimeline
import com.example.ui.components.BarrierStatusBadge
import com.example.ui.components.EmptyStateView
import com.example.ui.theme.AccessibleAmber
import com.example.ui.theme.AccessibleGreen
import com.example.ui.theme.AccessibleRed

@Composable
fun BarrierReportsScreen(
    reports: List<BarrierReport>,
    statusFilter: BarrierStatus?,
    isDialogOpen: Boolean,
    prefilledLocation: Pair<String, String>?,
    onFilterStatus: (BarrierStatus?) -> Unit,
    onOpenDialog: () -> Unit,
    onCloseDialog: () -> Unit,
    onSubmitReport: (String, BarrierCategory, String, String, String, BarrierSeverity) -> Unit,
    onDeleteReport: (BarrierReport) -> Unit,
    modifier: Modifier = Modifier
) {
    val statusScrollState = rememberScrollState()
    var reportToDelete by remember { mutableStateOf<BarrierReport?>(null) }

    val filteredReports by remember(reports, statusFilter) {
        derivedStateOf {
            if (statusFilter == null) reports
            else reports.filter { it.status == statusFilter }
        }
    }

    Column(
        modifier = modifier
            .testTag("barrier_reports_screen")
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Barrier Reports",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Report barriers & follow their repair progress",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            AccessibleButton(
                text = "Send Report",
                icon = Icons.Default.Add,
                onClick = onOpenDialog,
                isPrimary = true,
                testTag = "report_new_barrier_button"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Status Filter Chips Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(statusScrollState),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = statusFilter == null,
                onClick = { onFilterStatus(null) },
                label = { Text("All Reports (${reports.size})") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.primary
                )
            )

            BarrierStatus.entries.forEach { status ->
                val count = reports.count { it.status == status }
                FilterChip(
                    selected = statusFilter == status,
                    onClick = { onFilterStatus(status) },
                    label = { Text("${status.label} ($count)") }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Reports list
        if (filteredReports.isEmpty()) {
            EmptyStateView(
                title = "No Reports Under This Status",
                description = "All reports under this status have either been resolved or not yet submitted.",
                icon = Icons.Default.ReportProblem,
                actionButtonText = "Send a Barrier Report",
                onActionClick = onOpenDialog
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredReports, key = { it.id }) { report ->
                    BarrierReportItemCard(
                        report = report,
                        onDelete = { reportToDelete = report }
                    )
                }
            }
        }
    }

    // Confirmation dialog before deleting a report
    if (reportToDelete != null) {
        AccessibleConfirmationDialog(
            title = "Remove This Report?",
            description = "Are you sure you want to remove the report for \"${reportToDelete?.title}\"? This action cannot be undone.",
            confirmButtonText = "Remove Report",
            cancelButtonText = "Keep Report",
            isDestructive = true,
            icon = Icons.Default.DeleteOutline,
            onConfirm = {
                reportToDelete?.let { onDeleteReport(it) }
                reportToDelete = null
            },
            onDismiss = { reportToDelete = null }
        )
    }

    if (isDialogOpen) {
        NewBarrierReportDialog(
            prefilledLocation = prefilledLocation,
            onDismiss = onCloseDialog,
            onSubmit = onSubmitReport
        )
    }
}

@Composable
private fun BarrierReportItemCard(
    report: BarrierReport,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("barrier_report_${report.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header: Category & Status Badge & Delete
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = report.category.label,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    BarrierStatusBadge(status = report.status)
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .size(36.dp)
                            .semantics {
                                contentDescription = "Delete report: ${report.title}"
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = report.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${report.locationName}, ${report.city}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = report.specificLocationDetails,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Progress Timeline
            Text(
                text = "Resolution Timeline:",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            BarrierProgressTimeline(currentStatus = report.status)

            Spacer(modifier = Modifier.height(14.dp))

            // Severity & Date Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val severityColor = when (report.severity) {
                        BarrierSeverity.URGENT -> AccessibleRed
                        BarrierSeverity.HIGH -> AccessibleRed
                        BarrierSeverity.MEDIUM -> AccessibleAmber
                        BarrierSeverity.LOW -> MaterialTheme.colorScheme.primary
                    }
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = severityColor,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Severity: ${report.severity.label}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = severityColor
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Reported: ${report.dateReported}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Progress Update Box
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Review Status Update",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = report.statusNotes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (report.resolutionEstimate != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = report.resolutionEstimate,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewBarrierReportDialog(
    prefilledLocation: Pair<String, String>?,
    onDismiss: () -> Unit,
    onSubmit: (String, BarrierCategory, String, String, String, BarrierSeverity) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var locationName by remember { mutableStateOf(prefilledLocation?.first ?: "") }
    var city by remember { mutableStateOf(prefilledLocation?.second ?: "Bengaluru") }
    var details by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(BarrierCategory.BROKEN_RAMP) }
    var selectedSeverity by remember { mutableStateOf(BarrierSeverity.HIGH) }

    var categoryDropdownExpanded by remember { mutableStateOf(false) }
    var severityDropdownExpanded by remember { mutableStateOf(false) }
    var hasAttemptedSubmit by remember { mutableStateOf(false) }

    val isTitleEmpty = title.trim().isEmpty()
    val isLocationEmpty = locationName.trim().isEmpty()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Report an Accessibility Barrier",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            val dialogScroll = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(dialogScroll)
            ) {
                Text(
                    text = "Help make public spaces accessible by documenting physical or communication obstacles:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                AccessibleTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = "What is the obstacle?",
                    placeholder = "e.g. Broken ramp at entry gate",
                    errorMessage = if (hasAttemptedSubmit && isTitleEmpty) "Please describe what is broken or inaccessible." else null
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Category Dropdown
                ExposedDropdownMenuBox(
                    expanded = categoryDropdownExpanded,
                    onExpandedChange = { categoryDropdownExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedCategory.label,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Obstacle Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = categoryDropdownExpanded,
                        onDismissRequest = { categoryDropdownExpanded = false }
                    ) {
                        BarrierCategory.entries.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.label) },
                                onClick = {
                                    selectedCategory = cat
                                    categoryDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                AccessibleTextField(
                    value = locationName,
                    onValueChange = { locationName = it },
                    label = "Building, Station or Place Name",
                    placeholder = "e.g. Majestic Metro Station",
                    errorMessage = if (hasAttemptedSubmit && isLocationEmpty) "Please specify where this obstacle is located." else null
                )

                Spacer(modifier = Modifier.height(10.dp))

                AccessibleTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = "City",
                    placeholder = "e.g. Bengaluru, Delhi, Mumbai"
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Severity Dropdown
                ExposedDropdownMenuBox(
                    expanded = severityDropdownExpanded,
                    onExpandedChange = { severityDropdownExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedSeverity.label,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("How Serious Is It?") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = severityDropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = severityDropdownExpanded,
                        onDismissRequest = { severityDropdownExpanded = false }
                    ) {
                        BarrierSeverity.entries.forEach { sev ->
                            DropdownMenuItem(
                                text = { Text(sev.label) },
                                onClick = {
                                    selectedSeverity = sev
                                    severityDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                AccessibleTextField(
                    value = details,
                    onValueChange = { details = it },
                    label = "Specific Details & Exact Spot",
                    placeholder = "Describe where this barrier is located and who it affects...",
                    singleLine = false,
                    maxLines = 4,
                    modifier = Modifier.height(100.dp)
                )
            }
        },
        confirmButton = {
            AccessibleButton(
                text = "Send Report",
                onClick = {
                    hasAttemptedSubmit = true
                    if (!isTitleEmpty && !isLocationEmpty) {
                        onSubmit(title, selectedCategory, locationName, city, details, selectedSeverity)
                    }
                },
                testTag = "submit_barrier_form_button"
            )
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.height(48.dp)
            ) {
                Text("Cancel")
            }
        }
    )
}
