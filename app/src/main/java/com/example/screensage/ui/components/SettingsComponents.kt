package com.example.screensage.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Data class representing a radio button option
 */
data class RadioOption(
    val value: String,
    val label: String,
    val description: String? = null
)

/**
 * Segmented button group for mutually exclusive options
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SegmentedButtonGroup(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier.fillMaxWidth()
    ) {
        options.forEachIndexed { index, option ->
            SegmentedButton(
                selected = selectedOption.equals(option, ignoreCase = true),
                onClick = { onOptionSelected(option.lowercase()) },
                shape = when (index) {
                    0 -> RoundedCornerShape(
                        topStart = 24.dp,
                        bottomStart = 24.dp,
                        topEnd = 0.dp,
                        bottomEnd = 0.dp
                    )
                    options.size - 1 -> RoundedCornerShape(
                        topStart = 0.dp,
                        bottomStart = 0.dp,
                        topEnd = 24.dp,
                        bottomEnd = 24.dp
                    )
                    else -> RoundedCornerShape(0.dp)
                },
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = option,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

/**
 * Radio button group with vertical layout
 */
@Composable
fun RadioButtonGroup(
    title: String,
    options: List<RadioOption>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        options.forEachIndexed { index, option ->
            RadioButtonItem(
                option = option,
                selected = selectedOption.equals(option.value, ignoreCase = true),
                onClick = { onOptionSelected(option.value) }
            )
            
            if (index < options.size - 1) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
                )
            }
        }
    }
}

/**
 * Individual radio button item
 */
@Composable
private fun RadioButtonItem(
    option: RadioOption,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            modifier = Modifier.size(20.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = option.label,
                style = MaterialTheme.typography.bodyMedium,
                color = if (selected) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.onSurface
            )
            
            option.description?.let { desc ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

/**
 * State for the animated save button
 */
data class SaveButtonState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isError: Boolean = false
)

/**
 * Animated save button with loading, success, and error states
 */
@Composable
fun AnimatedSaveButton(
    onClick: suspend () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var buttonState by remember { mutableStateOf(SaveButtonState()) }
    val coroutineScope = rememberCoroutineScope()
    
    // Animation values
    val scale by animateFloatAsState(
        targetValue = if (buttonState.isSuccess) 1.0f else 0.95f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )
    
    val backgroundColor by animateColorAsState(
        targetValue = when {
            buttonState.isSuccess -> Color(0xFF4CAF50) // Success green
            buttonState.isError -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.primary
        },
        animationSpec = tween(durationMillis = 300),
        label = "backgroundColor"
    )
    
    Button(
        onClick = {
            if (!buttonState.isLoading && enabled) {
                coroutineScope.launch {
                    buttonState = SaveButtonState(isLoading = true)
                    
                    try {
                        onClick()
                        buttonState = SaveButtonState(isSuccess = true)
                        
                        // Return to default state after delay
                        delay(1500)
                        buttonState = SaveButtonState()
                    } catch (e: Exception) {
                        buttonState = SaveButtonState(isError = true)
                        
                        // Return to default state after delay
                        delay(2000)
                        buttonState = SaveButtonState()
                    }
                }
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .scale(scale),
        enabled = !buttonState.isLoading && enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        AnimatedContent(
            targetState = buttonState,
            transitionSpec = {
                fadeIn(animationSpec = tween(200)) togetherWith
                        fadeOut(animationSpec = tween(200))
            },
            label = "buttonContent"
        ) { state ->
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                }
                state.isSuccess -> {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Saved!")
                    }
                }
                state.isError -> {
                    Text("Error - Retry")
                }
                else -> {
                    Text("Save Settings")
                }
            }
        }
    }
}
