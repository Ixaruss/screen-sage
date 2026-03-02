package com.example.screensage

import android.R
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.OutlinedCard
import coil.compose.AsyncImage
import com.example.screensage.preferences.PreferencesManager
import com.example.screensage.ui.theme.ScreenSageTheme
import com.example.screensage.ui.theme.SuccessGreen
import com.example.screensage.utils.PermissionHelper
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private lateinit var preferencesManager: PreferencesManager
    private var shouldOpenSettings = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferencesManager = PreferencesManager(this)
        enableEdgeToEdge()
        
        // Check if we should open settings directly
        shouldOpenSettings = intent?.getBooleanExtra("open_settings", false) ?: false
        
        setContent {
            val coroutineScope = rememberCoroutineScope()
            var themePreference by remember { mutableStateOf("system") }
            var showSettings by remember { mutableStateOf(shouldOpenSettings) }
            
            // Reset the flag after using it
            LaunchedEffect(shouldOpenSettings) {
                if (shouldOpenSettings) {
                    showSettings = true
                    shouldOpenSettings = false
                }
            }
            
            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    themePreference = preferencesManager.getTheme()
                }
            }
            
            val darkTheme = when (themePreference) {
                "dark" -> true
                "light" -> false
                else -> isSystemInDarkTheme()
            }
            
            ScreenSageTheme(darkTheme = darkTheme) {
                val pagerState = rememberPagerState(
                    initialPage = if (showSettings) 3 else 0,
                    pageCount = { 4 }
                )
                
                LaunchedEffect(pagerState.currentPage) {
                    // No-op, just observe page changes
                }
                
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        AdaptiveNavigationBar(preferencesManager = preferencesManager) {
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                label = { Text("Home") },
                                selected = pagerState.currentPage == 0,
                                onClick = { 
                                    if (pagerState.currentPage != 0) {
                                        coroutineScope.launch { 
                                            pagerState.animateScrollToPage(
                                                0,
                                                animationSpec = androidx.compose.animation.core.tween(durationMillis = 250)
                                            )
                                        }
                                    }
                                }
                            )
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.DateRange, contentDescription = "History") },
                                label = { Text("History") },
                                selected = pagerState.currentPage == 1,
                                onClick = { 
                                    if (pagerState.currentPage != 1) {
                                        coroutineScope.launch { 
                                            pagerState.animateScrollToPage(
                                                1,
                                                animationSpec = androidx.compose.animation.core.tween(durationMillis = 250)
                                            )
                                        }
                                    }
                                }
                            )
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.Star, contentDescription = "Backgrounds") },
                                label = { Text("Backgrounds") },
                                selected = pagerState.currentPage == 2,
                                onClick = { 
                                    if (pagerState.currentPage != 2) {
                                        coroutineScope.launch { 
                                            pagerState.animateScrollToPage(
                                                2,
                                                animationSpec = androidx.compose.animation.core.tween(durationMillis = 250)
                                            )
                                        }
                                    }
                                }
                            )
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                                label = { Text("Settings") },
                                selected = pagerState.currentPage == 3,
                                onClick = { 
                                    if (pagerState.currentPage != 3) {
                                        coroutineScope.launch { 
                                            pagerState.animateScrollToPage(
                                                3,
                                                animationSpec = androidx.compose.animation.core.tween(durationMillis = 250)
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize(),
                        key = { it }
                    ) { page ->
                        when (page) {
                            0 -> BackgroundWrapper(
                                preferencesManager = preferencesManager,
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                HomeScreen(
                                    preferencesManager = preferencesManager,
                                    onStartService = { startOverlayService() },
                                    onStopService = { stopOverlayService() },
                                    onNavigateToSettings = { 
                                        coroutineScope.launch { pagerState.scrollToPage(3) }
                                    },
                                    onNavigateToBackgrounds = { 
                                        coroutineScope.launch { pagerState.scrollToPage(2) }
                                    }
                                )
                            }
                            1 -> BackgroundWrapper(
                                preferencesManager = preferencesManager,
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                HistoryScreen()
                            }
                            2 -> BackgroundWrapper(
                                preferencesManager = preferencesManager,
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                BackgroundsScreen(
                                    preferencesManager = preferencesManager,
                                    onBackgroundChanged = {
                                        // Trigger recomposition by recreating
                                        recreate()
                                    }
                                )
                            }
                            3 -> BackgroundWrapper(
                                preferencesManager = preferencesManager,
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                SettingsScreen(
                                    preferencesManager = preferencesManager,
                                    onStartService = { startOverlayService() },
                                    onStopService = { stopOverlayService() },
                                    onOpenAccessibilitySettings = { openAccessibilitySettings() },
                                    onThemeChanged = { newTheme ->
                                        themePreference = newTheme
                                    },
                                    onNavigateToHome = { 
                                        coroutineScope.launch { pagerState.scrollToPage(0) }
                                    },
                                    initialShowSettings = true,
                                    onShowSettingsChange = {}
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        // Handle the intent when activity is already running
        shouldOpenSettings = intent.getBooleanExtra("open_settings", false)
        if (shouldOpenSettings) {
            // Trigger recomposition by recreating
            recreate()
        }
    }

    private fun startOverlayService() {
        // Check if overlay permission is granted
        if (!PermissionHelper.hasOverlayPermission(this)) {
            PermissionHelper.requestOverlayPermission(this)
            return
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            startForegroundService(Intent(this, com.example.screensage.service.OverlayService::class.java))
        } else {
            startService(Intent(this, com.example.screensage.service.OverlayService::class.java))
        }
    }

    private fun stopOverlayService() {
        stopService(Intent(this, com.example.screensage.service.OverlayService::class.java))
    }

    private fun openAccessibilitySettings() {
        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
    }
}

@Composable
fun SettingsScreen(
    preferencesManager: PreferencesManager,
    onStartService: () -> Unit,
    onStopService: () -> Unit,
    onOpenAccessibilitySettings: () -> Unit,
    onThemeChanged: (String) -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    initialShowSettings: Boolean = false,
    onShowSettingsChange: (Boolean) -> Unit = {}
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var apiKey by remember { mutableStateOf("") }
    var theme by remember { mutableStateOf("system") }
    var model by remember { mutableStateOf("") }
    var provider by remember { mutableStateOf("gemini") }
    var overlayColor by remember { mutableStateOf("") }
    var systemPromptPreset by remember { mutableStateOf("concise") }
    var customSystemPrompt by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            apiKey = preferencesManager.getApiKey() ?: ""
            theme = preferencesManager.getTheme()
            model = preferencesManager.getModel() ?: ""
            provider = preferencesManager.getProvider()
            overlayColor = preferencesManager.getOverlayColor()
            systemPromptPreset = preferencesManager.getSystemPromptPreset()
            customSystemPrompt = preferencesManager.getCustomSystemPrompt()
        }
    }

    SettingsPanel(
        apiKey = apiKey,
        onApiKeyChange = { apiKey = it },
        theme = theme,
        onThemeChange = { theme = it },
        model = model,
        onModelChange = { model = it },
        provider = provider,
        onProviderChange = { provider = it },
        overlayColor = overlayColor,
        onOverlayColorChange = { overlayColor = it },
        systemPromptPreset = systemPromptPreset,
        onSystemPromptPresetChange = { systemPromptPreset = it },
        customSystemPrompt = customSystemPrompt,
        onCustomSystemPromptChange = { customSystemPrompt = it },
        onSave = {
            coroutineScope.launch {
                preferencesManager.setApiKey(apiKey)
                preferencesManager.setTheme(theme)
                if (model.isNotEmpty()) {
                    preferencesManager.setModel(model)
                }
                preferencesManager.setProvider(provider)
                preferencesManager.setOverlayColor(overlayColor)
                preferencesManager.setSystemPromptPreset(systemPromptPreset)
                preferencesManager.setCustomSystemPrompt(customSystemPrompt)
                onThemeChanged(theme)
                onNavigateToHome()
            }
        },
        onStartService = onStartService,
        onStopService = onStopService,
        onOpenAccessibilitySettings = onOpenAccessibilitySettings,
        onClose = { /* No-op since we're using bottom navigation */ }
    )
}

@Composable
fun PermissionDialog(
    onDismiss: () -> Unit,
    onGrantPermission: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Accessibility Permission Required") },
        text = {
            Column {
                Text("This service requires accessibility permission.\n")
                Text("Why?", style = androidx.compose.material3.MaterialTheme.typography.titleSmall)
                Text("• To create an overlay on screen")
                Text("• To detect selected text beyond this app\n")
                Text("Note: All data is stored locally and never shared outside the system.", 
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            }
        },
        confirmButton = {
            androidx.compose.material3.Button(onClick = onGrantPermission) {
                Text("Grant Permission")
            }
        },
        dismissButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun HomeScreen(
    onSettingsClick: () -> Unit,
    onToggleService: () -> Unit,
    isServiceRunning: Boolean,
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val chatHistoryManager = remember { com.example.screensage.storage.ChatHistoryManager(context) }
    var chatSessions by remember { mutableStateOf<List<com.example.screensage.models.ChatSession>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            chatSessions = chatHistoryManager.getAllSessions()
        }
    }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Screen Sage",
                    style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground
                )
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = androidx.compose.material3.MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Start/Stop button
            Button(
                onClick = onToggleService,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isServiceRunning) "Stop Overlay" else "Start Overlay")
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Chat history section
            if (chatSessions.isNotEmpty()) {
                Text(
                    text = "Recent Chats",
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                androidx.compose.foundation.lazy.LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(chatSessions.size) { index ->
                        val session = chatSessions[index]
                        ChatHistoryItem(
                            session = session,
                            onDelete = {
                                coroutineScope.launch {
                                    chatHistoryManager.deleteSession(session.id)
                                    chatSessions = chatHistoryManager.getAllSessions()
                                }
                            },
                            onClick = {
                                // Restore the chat session in the overlay
                                val intent = android.content.Intent(com.example.screensage.service.OverlayService.ACTION_RESTORE_SESSION)
                                intent.putExtra(com.example.screensage.service.OverlayService.EXTRA_SESSION_ID, session.id)
                                androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            } else {
                // Empty state
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No chat history yet",
                        style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Start a conversation to see it here",
                        style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatHistoryItem(
    session: com.example.screensage.models.ChatSession,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = session.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${session.messages.size} messages • ${formatTimestamp(session.updatedAt)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
    
    if (showDeleteDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Chat") },
            text = { Text("Are you sure you want to delete \"${session.title}\"? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        onDelete()
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60000 -> "Just now"
        diff < 3600000 -> "${diff / 60000}m ago"
        diff < 86400000 -> "${diff / 3600000}h ago"
        diff < 604800000 -> "${diff / 86400000}d ago"
        else -> "${diff / 604800000}w ago"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPanel(
    apiKey: String,
    onApiKeyChange: (String) -> Unit,
    theme: String,
    onThemeChange: (String) -> Unit,
    model: String,
    onModelChange: (String) -> Unit,
    provider: String,
    onProviderChange: (String) -> Unit,
    overlayColor: String,
    onOverlayColorChange: (String) -> Unit,
    systemPromptPreset: String,
    onSystemPromptPresetChange: (String) -> Unit,
    customSystemPrompt: String,
    onCustomSystemPromptChange: (String) -> Unit,
    onSave: () -> Unit,
    onStartService: () -> Unit,
    onStopService: () -> Unit,
    onOpenAccessibilitySettings: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("General", "Appearance & Behavior")
    
    Surface(
        modifier = modifier.fillMaxSize(),
        color = androidx.compose.ui.graphics.Color.Transparent
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top bar with title and back button
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
            
            // Tab row
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { 
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleSmall
                            ) 
                        }
                    )
                }
            }
            
            // Content based on selected tab
            when (selectedTab) {
                0 -> GeneralSettingsContent(
                    apiKey = apiKey,
                    onApiKeyChange = onApiKeyChange,
                    provider = provider,
                    onProviderChange = onProviderChange,
                    model = model,
                    onModelChange = onModelChange,
                    onOpenAccessibilitySettings = onOpenAccessibilitySettings,
                    onSave = onSave,
                    modifier = Modifier.weight(1f)
                )
                1 -> AppearanceSettingsContent(
                    theme = theme,
                    onThemeChange = onThemeChange,
                    overlayColor = overlayColor,
                    onOverlayColorChange = onOverlayColorChange,
                    systemPromptPreset = systemPromptPreset,
                    onSystemPromptPresetChange = onSystemPromptPresetChange,
                    customSystemPrompt = customSystemPrompt,
                    onCustomSystemPromptChange = onCustomSystemPromptChange,
                    onSave = onSave,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun GeneralSettingsContent(
    apiKey: String,
    onApiKeyChange: (String) -> Unit,
    provider: String,
    onProviderChange: (String) -> Unit,
    model: String,
    onModelChange: (String) -> Unit,
    onOpenAccessibilitySettings: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // AI Configuration Section
        SettingsSectionHeader(title = "AI Configuration")
        Spacer(modifier = Modifier.height(12.dp))

        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // API Key field - only show for non-local providers
                if (provider.lowercase() != "local") {
                    OutlinedTextField(
                        value = apiKey,
                        onValueChange = onApiKeyChange,
                        label = { Text("API Key") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        supportingText = { Text("Enter your AI provider API key", style = MaterialTheme.typography.bodySmall) }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                // Provider radio button group
                com.example.screensage.ui.components.RadioButtonGroup(
                    title = "AI Provider",
                    options = listOf(
                        com.example.screensage.ui.components.RadioOption(
                            value = "gemini",
                            label = "Gemini",
                            description = "Google's AI model with fast responses"
                        ),
                        com.example.screensage.ui.components.RadioOption(
                            value = "chatgpt",
                            label = "ChatGPT",
                            description = "OpenAI's conversational AI"
                        ),
                        com.example.screensage.ui.components.RadioOption(
                            value = "claude",
                            label = "Claude",
                            description = "Anthropic's helpful AI assistant"
                        ),
                        com.example.screensage.ui.components.RadioOption(
                            value = "local",
                            label = "Local (On-Device)",
                            description = "Privacy-focused offline model"
                        )
                    ),
                    selectedOption = provider,
                    onOptionSelected = { newProvider ->
                        onProviderChange(newProvider)
                        onModelChange("")
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Model selection based on provider
                val availableModels = when (provider.lowercase()) {
                    "gemini" -> listOf(
                        "gemini-3-flash-preview",
                        "gemini-1.5-flash",
                        "gemini-1.5-pro",
                        "gemini-1.0-pro"
                    )
                    "chatgpt" -> listOf(
                        "gpt-4o",
                        "gpt-4o-mini",
                        "gpt-4-turbo",
                        "gpt-4",
                        "gpt-3.5-turbo"
                    )
                    "claude" -> listOf(
                        "claude-3-5-sonnet-20241022",
                        "claude-3-5-haiku-20241022",
                        "claude-3-opus-20240229",
                        "claude-3-sonnet-20240229",
                        "claude-3-haiku-20240307"
                    )
                    else -> emptyList()
                }
                
                // Show model dropdown only for non-local providers
                if (provider.lowercase() != "local") {
                    var modelExpanded by remember { mutableStateOf(false) }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = model.ifEmpty { availableModels.firstOrNull() ?: "" },
                            onValueChange = {},
                            label = { Text("Model") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { modelExpanded = !modelExpanded }) {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowDropDown,
                                        contentDescription = "Model dropdown"
                                    )
                                }
                            }
                        )
                        DropdownMenu(
                            expanded = modelExpanded,
                            onDismissRequest = { modelExpanded = false }
                        ) {
                            availableModels.forEach { modelName ->
                                DropdownMenuItem(
                                    text = { Text(modelName) },
                                    onClick = {
                                        onModelChange(modelName)
                                        modelExpanded = false
                                    }
                                )
                            }
                        }
                    }
                } else {
                    // Show local model info
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        val context = androidx.compose.ui.platform.LocalContext.current
                        val localModelManager = remember { com.example.screensage.ai.LocalModelManager(context) }
                        var isModelDownloaded by remember { mutableStateOf(localModelManager.isModelDownloaded()) }
                        var isDownloading by remember { mutableStateOf(false) }
                        var downloadProgress by remember { mutableStateOf(0f) }
                        var downloadedMB by remember { mutableStateOf(0L) }
                        var totalMB by remember { mutableStateOf(676L) }
                        var downloadError by remember { mutableStateOf<String?>(null) }
                        
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "📱 Local Model: Gemma3-1B",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "System Requirements:",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "• Storage: 676MB free available",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "• RAM: 4GB minimum (6GB+ recommended)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "• Speed: 1-3 seconds per response",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "✓ No API key needed\n✓ Works offline\n✓ 100% private",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            // Download button or status
                            when {
                                isModelDownloaded -> {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "✓ Model Downloaded",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        TextButton(onClick = {
                                            localModelManager.deleteModel()
                                            isModelDownloaded = false
                                        }) {
                                            Text("Delete")
                                        }
                                    }
                                }
                                isDownloading -> {
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "Downloading...",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                            Text(
                                                text = "${downloadedMB}MB / ${totalMB}MB",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        androidx.compose.material3.LinearProgressIndicator(
                                            progress = downloadProgress,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                                else -> {
                                    Button(
                                        onClick = {
                                            isDownloading = true
                                            downloadError = null
                                            coroutineScope.launch {
                                                val result = localModelManager.downloadModel { progress, downloaded, total ->
                                                    downloadProgress = progress
                                                    downloadedMB = downloaded / (1024 * 1024)
                                                    totalMB = total / (1024 * 1024)
                                                }
                                                isDownloading = false
                                                result.fold(
                                                    onSuccess = {
                                                        isModelDownloaded = true
                                                    },
                                                    onFailure = { error ->
                                                        downloadError = error.message
                                                    }
                                                )
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Download Model (676MB)")
                                    }
                                }
                            }
                            
                            // Show error if any
                            downloadError?.let { error ->
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Error: $error",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Alternative: Download manually",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = "1. Visit: ai.google.dev/edge/mediapipe/solutions/genai/llm_inference/android\n2. Download gemma-2b-it-gpu-int4.bin\n3. Place in: ${localModelManager.getModelPath()}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Permissions Section
        val context = androidx.compose.ui.platform.LocalContext.current
        var hasOverlayPermission by remember { mutableStateOf(PermissionHelper.hasOverlayPermission(context)) }
        
        LaunchedEffect(Unit) {
            // Refresh permission status periodically
            while (true) {
                kotlinx.coroutines.delay(1000)
                hasOverlayPermission = PermissionHelper.hasOverlayPermission(context)
            }
        }
        
        SettingsSectionHeader(title = "Permissions")
        Spacer(modifier = Modifier.height(12.dp))
        
        // Overlay Permission Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Overlay Permission",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Required to display chat overlay on screen",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (hasOverlayPermission) "✓ Granted" else "✗ Not Granted",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (hasOverlayPermission) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.error
                        )
                    }
                }
                if (!hasOverlayPermission) {
                    Button(
                        onClick = { PermissionHelper.requestOverlayPermission(context) }
                    ) {
                        Text("Enable")
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Widget Section
        SettingsSectionHeader(title = "Home Screen Widget")
        Spacer(modifier = Modifier.height(12.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Quick Access Widget",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Add a widget to your home screen for quick access to Screen Sage",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        com.example.screensage.utils.WidgetHelper.requestPinWidget(context)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Widget to Home Screen")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Animated Save button
        com.example.screensage.ui.components.AnimatedSaveButton(
            onClick = {
                onSave()
            }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun AppearanceSettingsContent(
    theme: String,
    onThemeChange: (String) -> Unit,
    overlayColor: String,
    onOverlayColorChange: (String) -> Unit,
    systemPromptPreset: String,
    onSystemPromptPresetChange: (String) -> Unit,
    customSystemPrompt: String,
    onCustomSystemPromptChange: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Theme Section
        SettingsSectionHeader(title = "Theme")
        Spacer(modifier = Modifier.height(12.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "App Theme",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                com.example.screensage.ui.components.SegmentedButtonGroup(
                    options = listOf("Light", "Dark", "System"),
                    selectedOption = theme,
                    onOptionSelected = onThemeChange
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Overlay Appearance Section
        SettingsSectionHeader(title = "Overlay Appearance")
        Spacer(modifier = Modifier.height(12.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Overlay Color",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                com.example.screensage.ui.components.SegmentedButtonGroup(
                    options = listOf("Pink", "Blue", "Purple", "Green"),
                    selectedOption = overlayColor,
                    onOptionSelected = onOverlayColorChange
                )
                Text(
                    text = "Color theme for the chat overlay",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // AI Behavior Section
        SettingsSectionHeader(title = "AI Behavior")
        Spacer(modifier = Modifier.height(12.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Response Style",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Customize how the AI responds to your queries",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                com.example.screensage.ui.components.RadioButtonGroup(
                    title = "Response Preset",
                    options = listOf(
                        com.example.screensage.ui.components.RadioOption(
                            value = "concise",
                            label = "Concise",
                            description = "Brief, to-the-point explanations"
                        ),
                        com.example.screensage.ui.components.RadioOption(
                            value = "detailed",
                            label = "Detailed",
                            description = "Comprehensive, thorough explanations"
                        ),
                        com.example.screensage.ui.components.RadioOption(
                            value = "simple",
                            label = "Simple",
                            description = "Easy-to-understand, beginner-friendly"
                        ),
                        com.example.screensage.ui.components.RadioOption(
                            value = "technical",
                            label = "Technical",
                            description = "Advanced, technical terminology"
                        ),
                        com.example.screensage.ui.components.RadioOption(
                            value = "creative",
                            label = "Creative",
                            description = "Engaging, conversational tone"
                        ),
                        com.example.screensage.ui.components.RadioOption(
                            value = "custom",
                            label = "Custom",
                            description = "Define your own prompt"
                        )
                    ),
                    selectedOption = systemPromptPreset,
                    onOptionSelected = onSystemPromptPresetChange
                )
                
                // Show custom prompt input when "custom" is selected
                if (systemPromptPreset.lowercase() == "custom") {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = customSystemPrompt,
                        onValueChange = onCustomSystemPromptChange,
                        label = { Text("Custom System Prompt") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 6,
                        supportingText = { 
                            Text(
                                "Define how the AI should respond to your queries",
                                style = MaterialTheme.typography.bodySmall
                            ) 
                        }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Animated Save button
        com.example.screensage.ui.components.AnimatedSaveButton(
            onClick = {
                onSave()
            }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 4.dp)
    )
}

@Composable
fun FeatureItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun PermissionCard(
    title: String,
    description: String,
    isGranted: Boolean,
    onEnable: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            
            if (isGranted) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Granted",
                    tint = SuccessGreen,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                IconButton(onClick = onEnable) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Enable",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}


@Composable
fun HomeScreen(
    preferencesManager: PreferencesManager,
    onStartService: () -> Unit,
    onStopService: () -> Unit,
    onNavigateToSettings: () -> Unit = {},
    onNavigateToBackgrounds: () -> Unit = {}
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isServiceRunning by remember { mutableStateOf(false) }
    var provider by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var showPermissionDialog by remember { mutableStateOf(false) }
    var hasOverlayPermission by remember { mutableStateOf(false) }
    
    // Function to check if service is running
    fun isOverlayServiceRunning(): Boolean {
        val manager = context.getSystemService(android.content.Context.ACTIVITY_SERVICE) as android.app.ActivityManager
        @Suppress("DEPRECATION")
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (com.example.screensage.service.OverlayService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }
    
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            provider = preferencesManager.getProvider()
            val savedModel = preferencesManager.getModel()
            model = if (savedModel.isNullOrEmpty()) {
                // Get default model based on provider
                when (provider.lowercase()) {
                    "gemini" -> "gemini-1.5-flash"
                    "chatgpt" -> "gpt-4o-mini"
                    "claude" -> "claude-3-5-haiku-20241022"
                    "local" -> "Gemma 2B"
                    else -> "Not configured"
                }
            } else {
                savedModel
            }
            hasOverlayPermission = PermissionHelper.hasOverlayPermission(context)
            isServiceRunning = isOverlayServiceRunning()
        }
    }
    
    // Periodically check service status
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(1000) // Check every second
            isServiceRunning = isOverlayServiceRunning()
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top right buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = {
                        com.example.screensage.utils.WidgetHelper.requestPinWidget(context)
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Widget")
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = onNavigateToBackgrounds
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Background",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = "Screen Sage",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )

            // Subtitle
            Text(
                text = "Your Agentic AI Research Partner",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Works Offline Badge - Always visible
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "WORKS OFFLINE",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Large Circular Power Button
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clickable {
                        if (isServiceRunning) {
                            onStopService()
                        } else {
                            // Just start the service - no accessibility check needed
                            onStartService()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                // Outer circle
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = androidx.compose.foundation.shape.CircleShape,
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 8.dp
                ) {}

                // Inner circle
                Surface(
                    modifier = Modifier.size(160.dp),
                    shape = androidx.compose.foundation.shape.CircleShape,
                    color = if (isServiceRunning) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        androidx.compose.foundation.Image(
                            painter = androidx.compose.ui.res.painterResource(
                                id = context.resources.getIdentifier(
                                    "logo_bg_removed",
                                    "drawable",
                                    context.packageName
                                )
                            ),
                            contentDescription = "Power",
                            modifier = Modifier.size(128.dp),
                            colorFilter = if (isServiceRunning)
                                androidx.compose.ui.graphics.ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                            else
                                androidx.compose.ui.graphics.ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Status indicator
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            if (isServiceRunning) SuccessGreen else MaterialTheme.colorScheme.onBackground.copy(
                                alpha = 0.3f
                            ),
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isServiceRunning) "ACTIVE" else "INACTIVE",
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isServiceRunning) SuccessGreen else MaterialTheme.colorScheme.onBackground.copy(
                        alpha = 0.6f
                    ),
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isServiceRunning)
                    ""
                else
                    "Tap the button above to activate Screen Sage",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Features list
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FeatureItem("Summarize long paragraphs instantly")
                FeatureItem("Answer complex research queries")
                FeatureItem("Extract data from tables & charts")
                FeatureItem("Works offline via local models")
            }
            Spacer(modifier = Modifier.height(24.dp))

            Text (
            text =
                "Your data stays private. No data is sent to the cloud; all processing\nand storage happens locally on your device.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
        Spacer(modifier = Modifier.height(24.dp))
        
        // Permissions Section
        Text(
            text = "PERMISSIONS REQUIRED",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Permission Card - Only Overlay
        PermissionCard(
            title = "Display over other apps",
            description = "Required for the overlay chat assistant",
            isGranted = hasOverlayPermission,
            onEnable = { PermissionHelper.requestOverlayPermission(context) }
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Current Configuration
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToSettings() },
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "CURRENT CONFIGURATION",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Go to settings",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "AI MODEL",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = model.ifEmpty { "Not configured" },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                            )
                        }
                    }
                    
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "PROVIDER",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = provider.replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
        

        
        Spacer(modifier = Modifier.height(96.dp))
        
        // Quote of the Day
        QuoteOfTheDay()
        

        }
    }
    
    if (showPermissionDialog) {
        PermissionDialog(
            onDismiss = { showPermissionDialog = false },
            onGrantPermission = {
                showPermissionDialog = false
                val intent = android.content.Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS)
                context.startActivity(intent)
            }
        )
    }
}

data class Quote(
    val q: String,
    val a: String
)

@Composable
fun QuoteOfTheDay() {
    var quote by remember { mutableStateOf<Quote?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    
    LaunchedEffect(Unit) {
        coroutineScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            try {
                val url = java.net.URL("https://zenquotes.io/api/today")
                val connection = url.openConnection() as java.net.HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
                
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                
                // Parse JSON manually (simple parsing)
                val jsonArray = response.trim().removePrefix("[").removeSuffix("]")
                val qMatch = Regex("\"q\":\"([^\"]+)\"").find(jsonArray)
                val aMatch = Regex("\"a\":\"([^\"]+)\"").find(jsonArray)
                
                if (qMatch != null && aMatch != null) {
                    kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                        quote = Quote(qMatch.groupValues[1], aMatch.groupValues[1])
                        isLoading = false
                    }
                }
            } catch (e: Exception) {
                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                    isLoading = false
                }
            }
        }
    }
    
    if (!isLoading && quote != null) {
        Text(
            text = "\"${quote!!.q}\" — ${quote!!.a}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
        )
    }
}

@Composable
fun HistoryScreen() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val chatHistoryManager = remember { com.example.screensage.storage.ChatHistoryManager(context) }
    var chatSessions by remember { mutableStateOf<List<com.example.screensage.models.ChatSession>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            chatSessions = chatHistoryManager.getAllSessions()
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Chat History",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        if (chatSessions.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "No chat history yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Start a conversation to see it here",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                    )
                }
            }
        } else {
            androidx.compose.foundation.lazy.LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(chatSessions.size) { index ->
                    val session = chatSessions[index]
                    ChatHistoryItem(
                        session = session,
                        onDelete = {
                            coroutineScope.launch {
                                chatHistoryManager.deleteSession(session.id)
                                chatSessions = chatHistoryManager.getAllSessions()
                            }
                        },
                        onClick = {
                            // Check if service is running
                            val manager = context.getSystemService(android.content.Context.ACTIVITY_SERVICE) as android.app.ActivityManager
                            @Suppress("DEPRECATION")
                            val isServiceRunning = manager.getRunningServices(Integer.MAX_VALUE).any {
                                it.service.className == com.example.screensage.service.OverlayService::class.java.name
                            }
                            
                            // Start service if not running
                            if (!isServiceRunning) {
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                                    context.startForegroundService(android.content.Intent(context, com.example.screensage.service.OverlayService::class.java))
                                } else {
                                    context.startService(android.content.Intent(context, com.example.screensage.service.OverlayService::class.java))
                                }
                                // Wait a bit for service to start
                                coroutineScope.launch {
                                    kotlinx.coroutines.delay(500)
                                    // Restore the chat session in the overlay
                                    val intent = android.content.Intent(com.example.screensage.service.OverlayService.ACTION_RESTORE_SESSION)
                                    intent.putExtra(com.example.screensage.service.OverlayService.EXTRA_SESSION_ID, session.id)
                                    androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
                                }
                            } else {
                                // Restore the chat session in the overlay
                                val intent = android.content.Intent(com.example.screensage.service.OverlayService.ACTION_RESTORE_SESSION)
                                intent.putExtra(com.example.screensage.service.OverlayService.EXTRA_SESSION_ID, session.id)
                                androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BackgroundsScreen(
    preferencesManager: PreferencesManager,
    onBackgroundChanged: () -> Unit = {}
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var currentBackground by remember { mutableStateOf<String?>(null) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var pendingBackground by remember { mutableStateOf<String?>(null) }
    
    // Preset wallpapers list - add your wallpaper resource names here
    val presetWallpapers = listOf(
        "alexander",
        "black_red_gradient",
        "francesco_ungaro",
        "koyeldye",
        "no",
        "plane_sunset",
        "ss",
        "tchebotarev"
    )
    
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            currentBackground = preferencesManager.getBackgroundImage()
        }
    }
    
    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: android.net.Uri? ->
        uri?.let {
            pendingBackground = it.toString()
            showConfirmDialog = true
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Backgrounds",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            text = "Choose a background for Home and History screens",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Current selection
        if (currentBackground != null) {
            Text(
                text = "Current: ${if (currentBackground!!.startsWith("content://")) "Custom Image" else "Preset"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        
        // Preset wallpapers grid
        Text(
            text = "Preset Wallpapers",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(presetWallpapers.size) { index ->
                val wallpaper = presetWallpapers[index]
                val resourceId = context.resources.getIdentifier(
                    wallpaper,
                    "drawable",
                    context.packageName
                )
                
                Card(
                    modifier = Modifier
                        .aspectRatio(0.75f)
                        .clickable {
                            val resourceUri = "android.resource://${context.packageName}/$resourceId"
                            pendingBackground = resourceUri
                            showConfirmDialog = true
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (currentBackground == "android.resource://${context.packageName}/$resourceId") 
                            MaterialTheme.colorScheme.primaryContainer 
                        else 
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        if (resourceId != 0) {
                            androidx.compose.foundation.Image(
                                painter = androidx.compose.ui.res.painterResource(resourceId),
                                contentDescription = "Wallpaper $wallpaper",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Image not found",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                        
                        if (currentBackground == "android.resource://${context.packageName}/$resourceId") {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
        
        // Custom image section
        Text(
            text = "Custom Image",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        
        AdaptiveButton(
            onClick = { imagePickerLauncher.launch("image/*") },
            modifier = Modifier.fillMaxWidth(),
            preferencesManager = preferencesManager
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Choose Custom Image")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Clear background option
        AdaptiveButton(
            onClick = {
                pendingBackground = null
                showConfirmDialog = true
            },
            modifier = Modifier.fillMaxWidth(),
            preferencesManager = preferencesManager
        ) {
            Text("Clear Background")
        }
    }
    
    // Confirmation Dialog
    if (showConfirmDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { 
                showConfirmDialog = false
                pendingBackground = null
            },
            title = { Text("Change Background") },
            text = { 
                Text(
                    if (pendingBackground == null) 
                        "Are you sure you want to clear the background?" 
                    else 
                        "Apply this background? The app will refresh to show the new background."
                ) 
            },
            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            preferencesManager.setBackgroundImage(pendingBackground)
                            currentBackground = pendingBackground
                            showConfirmDialog = false
                            pendingBackground = null
                            // Trigger refresh
                            onBackgroundChanged()
                        }
                    }
                ) {
                    Text("Apply")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        showConfirmDialog = false
                        pendingBackground = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun BackgroundWrapper(
    preferencesManager: PreferencesManager,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val backgroundImage = remember {
        // Load once and cache
        kotlinx.coroutines.runBlocking {
            preferencesManager.getBackgroundImage()
        }
    }
    
    Box(modifier = modifier) {
        // Background image
        backgroundImage?.let { imageUri ->
            if (imageUri.startsWith("android.resource://")) {
                // Preset wallpaper
                val resourceId = imageUri.substringAfterLast("/").toIntOrNull()
                if (resourceId != null && resourceId != 0) {
                    androidx.compose.foundation.Image(
                        painter = androidx.compose.ui.res.painterResource(resourceId),
                        contentDescription = "Background",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                }
            } else {
                // Custom image from URI - Coil will cache this
                AsyncImage(
                    model = coil.request.ImageRequest.Builder(context)
                        .data(imageUri)
                        .memoryCacheKey(imageUri)
                        .diskCacheKey(imageUri)
                        .build(),
                    contentDescription = "Background",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            }
            
            // Semi-transparent overlay for readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.50f))
            )
        }
        
        // Content
        content()
    }
}

@Composable
fun GlassButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    // Glass button using available Backdrop API
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 12.dp,
            pressedElevation = 16.dp,
            disabledElevation = 0.dp
        ),
        shape = RoundedCornerShape(28.dp),
        content = content
    )
}

@Composable
fun AdaptiveButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    preferencesManager: PreferencesManager,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        content = content
    )
}

@Composable
fun AdaptiveNavigationBar(
    preferencesManager: PreferencesManager,
    content: @Composable RowScope.() -> Unit
) {
    androidx.compose.material3.NavigationBar(
        modifier = Modifier
            .height(85.dp)
            .fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 3.dp,
        content = content
    )
}

@Composable
fun AdaptiveCard(
    modifier: Modifier = Modifier,
    preferencesManager: PreferencesManager,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
        ),
        shape = RoundedCornerShape(12.dp),
        content = { content() }
    )
}
