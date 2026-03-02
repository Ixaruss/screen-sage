package com.example.screensage.preferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PreferencesManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences by lazy {
        try {
            // Try to create encrypted preferences
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            EncryptedSharedPreferences.create(
                context,
                PREFS_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            // Fallback to regular SharedPreferences if encryption fails
            Log.e("PreferencesManager", "Failed to create encrypted preferences, using regular SharedPreferences", e)
            
            // Try to delete corrupted encrypted preferences
            try {
                context.deleteSharedPreferences(PREFS_NAME)
            } catch (deleteException: Exception) {
                Log.e("PreferencesManager", "Failed to delete corrupted preferences", deleteException)
            }
            
            // Return regular SharedPreferences as fallback
            context.getSharedPreferences(PREFS_NAME_FALLBACK, Context.MODE_PRIVATE)
        }
    }

    suspend fun getApiKey(): String? = withContext(Dispatchers.IO) {
        try {
            sharedPreferences.getString(KEY_API_KEY, null)
        } catch (e: Exception) {
            Log.e("PreferencesManager", "Error reading API key", e)
            null
        }
    }

    suspend fun setApiKey(key: String) = withContext(Dispatchers.IO) {
        try {
            sharedPreferences.edit().putString(KEY_API_KEY, key).apply()
        } catch (e: Exception) {
            Log.e("PreferencesManager", "Error saving API key", e)
        }
    }

    suspend fun getTheme(): String = withContext(Dispatchers.IO) {
        try {
            sharedPreferences.getString(KEY_THEME, THEME_SYSTEM) ?: THEME_SYSTEM
        } catch (e: Exception) {
            Log.e("PreferencesManager", "Error reading theme", e)
            THEME_SYSTEM
        }
    }

    suspend fun setTheme(theme: String) = withContext(Dispatchers.IO) {
        try {
            sharedPreferences.edit().putString(KEY_THEME, theme).apply()
        } catch (e: Exception) {
            Log.e("PreferencesManager", "Error saving theme", e)
        }
    }

    suspend fun getModel(): String? = withContext(Dispatchers.IO) {
        try {
            sharedPreferences.getString(KEY_MODEL, null)
        } catch (e: Exception) {
            Log.e("PreferencesManager", "Error reading model", e)
            null
        }
    }

    suspend fun setModel(model: String) = withContext(Dispatchers.IO) {
        try {
            sharedPreferences.edit().putString(KEY_MODEL, model).apply()
        } catch (e: Exception) {
            Log.e("PreferencesManager", "Error saving model", e)
        }
    }

    suspend fun getProvider(): String = withContext(Dispatchers.IO) {
        try {
            sharedPreferences.getString(KEY_PROVIDER, PROVIDER_GEMINI) ?: PROVIDER_GEMINI
        } catch (e: Exception) {
            Log.e("PreferencesManager", "Error reading provider", e)
            PROVIDER_GEMINI
        }
    }

    suspend fun setProvider(provider: String) = withContext(Dispatchers.IO) {
        try {
            sharedPreferences.edit().putString(KEY_PROVIDER, provider).apply()
        } catch (e: Exception) {
            Log.e("PreferencesManager", "Error saving provider", e)
        }
    }

    fun getProviderSync(): String {
        return try {
            sharedPreferences.getString(KEY_PROVIDER, PROVIDER_GEMINI) ?: PROVIDER_GEMINI
        } catch (e: Exception) {
            Log.e("PreferencesManager", "Error reading provider sync", e)
            PROVIDER_GEMINI
        }
    }

    suspend fun getOverlayColor(): String = withContext(Dispatchers.IO) {
        try {
            sharedPreferences.getString(KEY_OVERLAY_COLOR, COLOR_PINK) ?: COLOR_PINK
        } catch (e: Exception) {
            Log.e("PreferencesManager", "Error reading overlay color", e)
            COLOR_PINK
        }
    }

    fun getOverlayColorSync(): String {
        return try {
            sharedPreferences.getString(KEY_OVERLAY_COLOR, COLOR_PINK) ?: COLOR_PINK
        } catch (e: Exception) {
            Log.e("PreferencesManager", "Error reading overlay color sync", e)
            COLOR_PINK
        }
    }

    suspend fun setOverlayColor(color: String) = withContext(Dispatchers.IO) {
        try {
            sharedPreferences.edit().putString(KEY_OVERLAY_COLOR, color).apply()
        } catch (e: Exception) {
            Log.e("PreferencesManager", "Error saving overlay color", e)
        }
    }

    suspend fun getSystemPromptPreset(): String = withContext(Dispatchers.IO) {
        try {
            sharedPreferences.getString(KEY_SYSTEM_PROMPT_PRESET, PRESET_CONCISE) ?: PRESET_CONCISE
        } catch (e: Exception) {
            Log.e("PreferencesManager", "Error reading system prompt preset", e)
            PRESET_CONCISE
        }
    }

    suspend fun setSystemPromptPreset(preset: String) = withContext(Dispatchers.IO) {
        try {
            sharedPreferences.edit().putString(KEY_SYSTEM_PROMPT_PRESET, preset).apply()
        } catch (e: Exception) {
            Log.e("PreferencesManager", "Error saving system prompt preset", e)
        }
    }

    suspend fun getCustomSystemPrompt(): String = withContext(Dispatchers.IO) {
        try {
            sharedPreferences.getString(KEY_CUSTOM_SYSTEM_PROMPT, "") ?: ""
        } catch (e: Exception) {
            Log.e("PreferencesManager", "Error reading custom system prompt", e)
            ""
        }
    }

    suspend fun setCustomSystemPrompt(prompt: String) = withContext(Dispatchers.IO) {
        try {
            sharedPreferences.edit().putString(KEY_CUSTOM_SYSTEM_PROMPT, prompt).apply()
        } catch (e: Exception) {
            Log.e("PreferencesManager", "Error saving custom system prompt", e)
        }
    }

    suspend fun getSystemPrompt(): String = withContext(Dispatchers.IO) {
        val preset = getSystemPromptPreset()
        when (preset.lowercase()) {
            PRESET_CONCISE -> "You are a helpful AI assistant. Provide brief, to-the-point explanations. Be concise and clear."
            PRESET_DETAILED -> "You are a helpful AI assistant. Provide comprehensive and thorough explanations. Include relevant details and context."
            PRESET_SIMPLE -> "You are a helpful AI assistant. Explain things in simple, easy-to-understand language. Avoid technical jargon and use beginner-friendly terms."
            PRESET_TECHNICAL -> "You are a helpful AI assistant. Provide technical explanations using advanced terminology. Assume the user has technical knowledge."
            PRESET_CREATIVE -> "You are a helpful AI assistant. Respond in an engaging, conversational tone. Be creative and personable in your explanations."
            PRESET_CUSTOM -> getCustomSystemPrompt().ifEmpty { "You are a helpful AI assistant." }
            else -> "You are a helpful AI assistant."
        }
    }

    suspend fun clearAllData() = withContext(Dispatchers.IO) {
        try {
            sharedPreferences.edit().clear().apply()
        } catch (e: Exception) {
            Log.e("PreferencesManager", "Error clearing data", e)
        }
    }

    suspend fun setBackgroundImage(imageUri: String?) = withContext(Dispatchers.IO) {
        try {
            if (imageUri != null) {
                sharedPreferences.edit().putString(KEY_BACKGROUND_IMAGE, imageUri).apply()
            } else {
                sharedPreferences.edit().remove(KEY_BACKGROUND_IMAGE).apply()
            }
        } catch (e: Exception) {
            Log.e("PreferencesManager", "Error saving background image", e)
        }
    }

    suspend fun getBackgroundImage(): String? = withContext(Dispatchers.IO) {
        try {
            sharedPreferences.getString(KEY_BACKGROUND_IMAGE, null)
        } catch (e: Exception) {
            Log.e("PreferencesManager", "Error reading background image", e)
            null
        }
    }

    companion object {
        private const val PREFS_NAME = "screen_sage_prefs"
        private const val PREFS_NAME_FALLBACK = "screen_sage_prefs_fallback"
        private const val KEY_API_KEY = "api_key"
        private const val KEY_THEME = "theme"
        private const val KEY_MODEL = "model"
        private const val KEY_PROVIDER = "provider"
        private const val KEY_OVERLAY_COLOR = "overlay_color"
        private const val KEY_SYSTEM_PROMPT_PRESET = "system_prompt_preset"
        private const val KEY_CUSTOM_SYSTEM_PROMPT = "custom_system_prompt"
        private const val KEY_BACKGROUND_IMAGE = "background_image"

        const val THEME_DARK = "dark"
        const val THEME_LIGHT = "light"
        const val THEME_SYSTEM = "system"

        const val PROVIDER_GEMINI = "gemini"
        const val PROVIDER_CHATGPT = "chatgpt"
        const val PROVIDER_CLAUDE = "claude"
        const val PROVIDER_LOCAL = "local"

        const val COLOR_PINK = "pink"
        const val COLOR_BLUE = "blue"
        const val COLOR_PURPLE = "purple"
        const val COLOR_GREEN = "green"

        const val PRESET_CONCISE = "concise"
        const val PRESET_DETAILED = "detailed"
        const val PRESET_SIMPLE = "simple"
        const val PRESET_TECHNICAL = "technical"
        const val PRESET_CREATIVE = "creative"
        const val PRESET_CUSTOM = "custom"
    }
}
