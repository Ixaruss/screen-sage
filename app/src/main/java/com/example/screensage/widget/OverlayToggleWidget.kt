package com.example.screensage.widget

import android.app.ActivityManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import com.example.screensage.MainActivity
import com.example.screensage.R
import com.example.screensage.service.OverlayService

class OverlayToggleWidget : AppWidgetProvider() {
    
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        
        when (intent.action) {
            ACTION_TOGGLE_OVERLAY -> {
                toggleOverlayService(context)
                
                // Update all widgets
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val appWidgetIds = appWidgetManager.getAppWidgetIds(
                    ComponentName(context, OverlayToggleWidget::class.java)
                )
                onUpdate(context, appWidgetManager, appWidgetIds)
            }
            ACTION_OPEN_SETTINGS -> {
                val settingsIntent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    putExtra("open_settings", true)
                }
                context.startActivity(settingsIntent)
            }
        }
    }

    private fun toggleOverlayService(context: Context) {
        val serviceIntent = Intent(context, OverlayService::class.java)
        
        if (isServiceRunning(context)) {
            context.stopService(serviceIntent)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.startForegroundService(serviceIntent)
            } else {
                context.startService(serviceIntent)
            }
        }
    }

    private fun isServiceRunning(context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        @Suppress("DEPRECATION")
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (OverlayService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }

    companion object {
        private const val ACTION_TOGGLE_OVERLAY = "com.example.screensage.widget.TOGGLE_OVERLAY"
        private const val ACTION_OPEN_SETTINGS = "com.example.screensage.widget.OPEN_SETTINGS"

        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.widget_overlay_toggle)

            // Check if service is running
            val isRunning = isServiceRunning(context)
            
            // Update status text (no color change)
            if (isRunning) {
                views.setTextViewText(R.id.widget_status, "ACTIVE")
            } else {
                views.setTextViewText(R.id.widget_status, "INACTIVE")
            }
            views.setTextColor(R.id.widget_status, 0xFFFFFFFF.toInt()) // Always white

            // Create intent for toggle action
            val toggleIntent = Intent(context, OverlayToggleWidget::class.java).apply {
                action = ACTION_TOGGLE_OVERLAY
            }
            val togglePendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                toggleIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            
            views.setOnClickPendingIntent(R.id.widget_container, togglePendingIntent)

            // Create intent for settings button
            val settingsIntent = Intent(context, OverlayToggleWidget::class.java).apply {
                action = ACTION_OPEN_SETTINGS
            }
            val settingsPendingIntent = PendingIntent.getBroadcast(
                context,
                1,
                settingsIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            
            views.setOnClickPendingIntent(R.id.widget_settings_button, settingsPendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun isServiceRunning(context: Context): Boolean {
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            @Suppress("DEPRECATION")
            for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
                if (OverlayService::class.java.name == service.service.className) {
                    return true
                }
            }
            return false
        }
    }
}
