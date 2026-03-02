package com.example.screensage.utils

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.widget.Toast
import com.example.screensage.widget.OverlayToggleWidget

object WidgetHelper {
    fun requestPinWidget(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val myProvider = ComponentName(context, OverlayToggleWidget::class.java)
            
            if (appWidgetManager.isRequestPinAppWidgetSupported) {
                // Request to pin the widget
                appWidgetManager.requestPinAppWidget(myProvider, null, null)
            } else {
                // Fallback to manual instructions
                Toast.makeText(
                    context,
                    "Long press on home screen > Widgets > Screen Sage",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            // For older Android versions, show manual instructions
            Toast.makeText(
                context,
                "Long press on home screen > Widgets > Screen Sage",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
