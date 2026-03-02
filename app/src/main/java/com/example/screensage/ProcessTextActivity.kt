package com.example.screensage

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity

class ProcessTextActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Get the selected text from the intent
        val selectedText = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)?.toString()
        
        if (!selectedText.isNullOrEmpty()) {
            // Format the query as: Explain "selected text"
            val formattedQuery = "Explain \"$selectedText\""
            
            // Start the overlay service with the formatted query
            val serviceIntent = Intent(this, com.example.screensage.service.OverlayService::class.java).apply {
                action = "ACTION_PROCESS_TEXT"
                putExtra("SELECTED_TEXT", formattedQuery)
            }
            startService(serviceIntent)
        }
        
        // Close this activity immediately
        finish()
    }
}
