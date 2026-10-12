package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.TextFormat
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.AccessibilityPreferences
import com.example.model.AppLanguage
import com.example.ui.components.AccessibleButton
import com.example.ui.components.AccessibleConfirmationDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesScreen(
    preferences: AccessibilityPreferences,
    onSetHighContrast: (Boolean) -> Unit,
    onSetFontScale: (Float) -> Unit,
    onSetDyslexiaSpacing: (Boolean) -> Unit,
    onSetReducedMotion: (Boolean) -> Unit,
    onSetLanguage: (AppLanguage) -> Unit,
    onSetSpeechRate: (Float) -> Unit,
    onSetHaptic: (Boolean) -> Unit,
    onTestSpeech: () -> Unit,
    onResetDefaults: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var languageDropdownExpanded by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .testTag("preferences_screen")
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Your Preferences",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Customize display, reading & speech settings to your comfort. Saved locally on this device.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Live Preview Box so user sees adjustments instantly
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Live Text & Spacing Preview",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "The quick brown fox jumps over the lazy dog. Notice how text size and spacing adjust seamlessly across the app.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Visual Comfort Section
        PreferenceCategoryCard(title = "Visual & Contrast Settings") {
            // High Contrast Switch
            PreferenceToggleRow(
                title = "High Contrast Mode",
                subtitle = "Uses stark dark backgrounds with vivid yellow & cyan for maximum visibility",
                icon = Icons.Default.Contrast,
                checked = preferences.highContrast,
                onCheckedChange = onSetHighContrast,
                testTag = "pref_high_contrast_switch"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Font Scale Slider
            Text(
                text = "Text Size Scale: ${(preferences.fontScale * 100).toInt()}%",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Enlarge or reduce typography across all screens",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "A", style = MaterialTheme.typography.titleSmall)
                Slider(
                    value = preferences.fontScale,
                    onValueChange = onSetFontScale,
                    valueRange = 1.0f..1.5f,
                    steps = 3,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                        .testTag("pref_font_scale_slider")
                )
                Text(text = "A", style = MaterialTheme.typography.headlineMedium)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Dyslexia-friendly Spacing
            PreferenceToggleRow(
                title = "Dyslexia-Friendly Letter Spacing",
                subtitle = "Expands line height and letter spacing for easier reading",
                icon = Icons.Default.TextFormat,
                checked = preferences.dyslexiaSpacing,
                onCheckedChange = onSetDyslexiaSpacing,
                testTag = "pref_dyslexia_spacing_switch"
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Reduced Motion
            PreferenceToggleRow(
                title = "Reduced Motion",
                subtitle = "Minimizes animations and decorative screen movements",
                icon = Icons.Default.Visibility,
                checked = preferences.reducedMotion,
                onCheckedChange = onSetReducedMotion,
                testTag = "pref_reduced_motion_switch"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Language & Speech Section
        PreferenceCategoryCard(title = "Language & Spoken Audio") {
            // Language Selection
            Text(
                text = "Preferred Display & Speech Language",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Translates communication cards and speaks in your chosen language",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = languageDropdownExpanded,
                onExpandedChange = { languageDropdownExpanded = it }
            ) {
                OutlinedTextField(
                    value = "${preferences.preferredLanguage.displayName} (${preferences.preferredLanguage.localName})",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Language") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = languageDropdownExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .testTag("pref_language_dropdown"),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = languageDropdownExpanded,
                    onDismissRequest = { languageDropdownExpanded = false }
                ) {
                    AppLanguage.entries.forEach { lang ->
                        DropdownMenuItem(
                            text = { Text("${lang.displayName} - ${lang.localName}") },
                            onClick = {
                                onSetLanguage(lang)
                                languageDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Speech Rate Slider
            Text(
                text = "Text-to-Speech Speed: ${String.format("%.2f", preferences.speechRate)}x",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Control how fast the spoken voice reads notices and phrases",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Slider(
                value = preferences.speechRate,
                onValueChange = onSetSpeechRate,
                valueRange = 0.75f..1.35f,
                steps = 2,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("pref_speech_rate_slider")
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Test Voice Reading Button
            AccessibleButton(
                text = "Test Spoken Voice",
                icon = Icons.Default.VolumeUp,
                onClick = onTestSpeech,
                isPrimary = false,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Haptics
            PreferenceToggleRow(
                title = "Vibration & Haptic Feedback",
                subtitle = "Subtle vibration when buttons and options are pressed",
                icon = Icons.Default.Vibration,
                checked = preferences.hapticFeedback,
                onCheckedChange = onSetHaptic,
                testTag = "pref_haptic_switch"
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Reset Settings Button with confirmation dialog
        AccessibleButton(
            text = "Reset to Standard Settings",
            icon = Icons.Default.RestartAlt,
            onClick = { showResetDialog = true },
            isPrimary = false,
            isDestructive = true,
            modifier = Modifier.fillMaxWidth(),
            testTag = "pref_reset_button"
        )

        Spacer(modifier = Modifier.height(28.dp))
    }

    if (showResetDialog) {
        AccessibleConfirmationDialog(
            title = "Reset Preferences?",
            description = "This will return contrast, text scale, language, and speech speed back to standard settings.",
            confirmButtonText = "Reset to Defaults",
            cancelButtonText = "Keep Current",
            isDestructive = true,
            icon = Icons.Default.RestartAlt,
            onConfirm = {
                onResetDefaults()
                showResetDialog = false
            },
            onDismiss = { showResetDialog = false }
        )
    }
}

@Composable
private fun PreferenceCategoryCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(14.dp))
            content()
        }
    }
}

@Composable
private fun PreferenceToggleRow(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 12.dp)
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.testTag(testTag)
        )
    }
}
